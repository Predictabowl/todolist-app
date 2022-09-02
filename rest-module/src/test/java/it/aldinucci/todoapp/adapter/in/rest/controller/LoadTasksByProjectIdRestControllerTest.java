package it.aldinucci.todoapp.adapter.in.rest.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
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

import it.aldinucci.todoapp.adapter.in.rest.security.config.AppRestSecurityConfig;
import it.aldinucci.todoapp.application.port.in.LoadTasksByProjectUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;
import it.aldinucci.todoapp.webcommons.handler.AppWebExceptionHandlers;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@WebMvcTest(controllers = { LoadTasksByProjectIdRestController.class })
@ExtendWith(SpringExtension.class)
@Import({AppRestSecurityConfig.class, AppWebExceptionHandlers.class})
class LoadTasksByProjectIdRestControllerTest {

	private static final String TEST_EMAIL_FIXTURE = "test@email.org";

	@Autowired
	private MockMvc mvc;

	@MockBean
	private LoadTasksByProjectUsePort usePort;
	
	@MockBean
	private InputModelAuthorization<ProjectIdDTO> authorize;

	@Test
	@WithMockUser(TEST_EMAIL_FIXTURE)
	void test_loadTasks_successful() throws Exception {
		Task task1 = new Task("2", "project 1", "description 1");
		Task task2 = new Task("7", "project 2", "description 2");
		when(usePort.load(isA(ProjectIdDTO.class))).thenReturn(Arrays.asList(task1, task2));
		
		mvc.perform(get("/api/project/3/tasks")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id", is("2")))
			.andExpect(jsonPath("$[0].name", is("project 1")))
			.andExpect(jsonPath("$[0].description", is("description 1")))
			.andExpect(jsonPath("$[1].id", is("7")))
			.andExpect(jsonPath("$[1].name", is("project 2")))
			.andExpect(jsonPath("$[1].description", is("description 2")));
		
		InOrder inOrder = Mockito.inOrder(usePort,authorize);
		inOrder.verify(authorize).check(new UserIdDTO(TEST_EMAIL_FIXTURE), new ProjectIdDTO("3"));
		inOrder.verify(usePort).load(new ProjectIdDTO("3"));
	}
	
	@Test
	@WithMockUser(TEST_EMAIL_FIXTURE)
	void test_loadTasks_whenProjectNotFound_shouldReturnNotFound() throws Exception {
		doThrow(new AppProjectNotFoundException("return message")).when(authorize)
			.check(isA(UserIdDTO.class), any());
		
		mvc.perform(get("/api/project/1/tasks")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$", is("return message")));
		
		verify(authorize).check(new UserIdDTO(TEST_EMAIL_FIXTURE), new ProjectIdDTO("1"));
		verifyNoInteractions(usePort);
	}
	
	@Test
	void test_loadTasks_withoutAuthentication_shouldReturnUnauthorized() throws Exception {
		
		mvc.perform(get("/api/project/1/tasks")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(usePort);
	}

}
