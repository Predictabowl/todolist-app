package it.aldinucci.todoapp.adapter.in.rest.controller;

import static it.aldinucci.todoapp.webcommons.config.AppBaseURIs.BASE_REST_URI;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import it.aldinucci.todoapp.application.port.in.LoadTasksByProjectUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exceptions.AppProjectNotFoundException;
import it.aldinucci.todoapp.webcommons.config.security.AppRestSecurityConfig;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@WebMvcTest(controllers = { LoadTaskByProjectIdRestController.class })
@ExtendWith(SpringExtension.class)
@Import(AppRestSecurityConfig.class)
class LoadTaskByProjectIdRestControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private LoadTasksByProjectUsePort usePort;
	
	@MockBean
	private InputModelAuthorization<ProjectIdDTO> authorize;

	@Test
	@WithMockUser("test@email.org")
	void test_loadTasks_successful() throws Exception {
		Task task1 = new Task(2L, "project 1", "description 1");
		Task task2 = new Task(7L, "project 2", "description 2");
		when(usePort.load(isA(ProjectIdDTO.class))).thenReturn(Arrays.asList(task1, task2));
		
		mvc.perform(get(BASE_REST_URI+"/project/3/tasks")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id", is(2)))
			.andExpect(jsonPath("$[0].name", is("project 1")))
			.andExpect(jsonPath("$[0].description", is("description 1")))
			.andExpect(jsonPath("$[1].id", is(7)))
			.andExpect(jsonPath("$[1].name", is("project 2")))
			.andExpect(jsonPath("$[1].description", is("description 2")));
		
		InOrder inOrder = Mockito.inOrder(usePort,authorize);
		inOrder.verify(authorize).check("test@email.org", new ProjectIdDTO(3L));
		inOrder.verify(usePort).load(new ProjectIdDTO(3L));
	}
	
	@Test
	@WithMockUser("testmail")
	void test_loadTasks_whenProjectNotFound_shouldReturnBadRequest() throws Exception {
		when(usePort.load(isA(ProjectIdDTO.class))).thenThrow(new AppProjectNotFoundException("return message"));
		
		mvc.perform(get(BASE_REST_URI+"/project/1/tasks")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$", is("return message")));
		
		InOrder inOrder = inOrder(usePort,authorize);
		ProjectIdDTO model = new ProjectIdDTO(1L);
		inOrder.verify(authorize).check("testmail", model);
		inOrder.verify(usePort).load(model);
	}
	
	@Test
	void test_loadTasks_withoutAuthentication_shouldReturnUnauthorized() throws Exception {
		
		mvc.perform(get(BASE_REST_URI+"/project/1/tasks")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(usePort);
	}

}
