package it.aldinucci.todoapp.adapter.in.rest.controller;


import static it.aldinucci.todoapp.webcommons.config.AppBaseURIs.BASE_REST_URI;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.aldinucci.todoapp.application.port.in.CreateTaskUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exceptions.AppProjectNotFoundException;
import it.aldinucci.todoapp.webcommons.config.security.AppRestSecurityConfig;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {CreateTaskRestController.class})
@Import({AppRestSecurityConfig.class})
class CreateTaskRestControllerTest {
	
	private static final String FIXTURE_URL = BASE_REST_URI+"/task/create";
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private InputModelAuthorization<NewTaskDTOIn> authorize;
	
	@MockBean
	private CreateTaskUsePort createTask;
	
	private ObjectMapper objectMapper;
	
	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
	}
	

	@Test
	@WithMockUser("user email")
	void test_createTask_successful() throws JsonProcessingException, Exception {
		Task task = new Task(1L, "new task", "task description");
		NewTaskDTOIn taskDto = new NewTaskDTOIn("test name", "test description", 2L);
		when(createTask.create(isA(NewTaskDTOIn.class)))
			.thenReturn(task);
		
		mvc.perform(post(FIXTURE_URL)
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(taskDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", is("new task")))
			.andExpect(jsonPath("$.description", is("task description")))
			.andExpect(jsonPath("$.id", is(1)));
		
		InOrder inOrder = Mockito.inOrder(createTask,authorize);
		inOrder.verify(authorize).check("user email", taskDto);
		inOrder.verify(createTask).create(taskDto);
	}
	

	@Test
	@WithMockUser
	void test_createTask_withEmptyName_shouldSendBadRequest() throws JsonProcessingException, Exception {
		NewTaskDTOIn taskDto = new NewTaskDTOIn("", "description",7L);
		
		mvc.perform(post(FIXTURE_URL)
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(taskDto)))
			.andExpect(status().isBadRequest());
		
		verifyNoInteractions(createTask);
		verifyNoInteractions(authorize);
	}
	
	@Test
	@WithMockUser("email")
	void test_createTask_whenProjectNotFound_shouldReturnBadRequest() throws JsonProcessingException, Exception {
		NewTaskDTOIn taskDto = new NewTaskDTOIn("test name", "description",11L);
		when(createTask.create(isA(NewTaskDTOIn.class)))
			.thenThrow(new AppProjectNotFoundException("test message"));
		
		mvc.perform(post(FIXTURE_URL)
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(taskDto)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$",is("test message")));
		
		InOrder inOrder = Mockito.inOrder(authorize,createTask);
		inOrder.verify(authorize).check("email", taskDto);
		inOrder.verify(createTask).create(taskDto);
	}

}
