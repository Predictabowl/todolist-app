package it.aldinucci.todoapp.adapter.in.rest.controller;


import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
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

import it.aldinucci.todoapp.adapter.in.rest.security.config.AppRestSecurityConfig;
import it.aldinucci.todoapp.application.port.in.CreateTaskUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;
import it.aldinucci.todoapp.webcommons.handler.AppWebExceptionHandlers;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {CreateTaskRestController.class})
@Import({AppRestSecurityConfig.class, AppWebExceptionHandlers.class})
class CreateTaskRestControllerTest {
	
	private static final String FIXTURE_URL = "/api/task/create";
	
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
		Task task = new Task("1", "new task", "task description");
		NewTaskDTOIn taskDto = new NewTaskDTOIn("test name", "test description", "2");
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
			.andExpect(jsonPath("$.id", is("1")));
		
		InOrder inOrder = Mockito.inOrder(createTask,authorize);
		inOrder.verify(authorize).check("user email", taskDto);
		inOrder.verify(createTask).create(taskDto);
	}
	

	@Test
	@WithMockUser
	void test_createTask_withEmptyName_shouldSendBadRequest() throws JsonProcessingException, Exception {
		String jsonTask = "{\"name\":\"\", \"description\":\"description\",\"projectId\":7}";
		
		mvc.perform(post(FIXTURE_URL)
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonTask))
			.andExpect(status().isBadRequest());
		
		verifyNoInteractions(createTask);
		verifyNoInteractions(authorize);
	}
	
	@Test
	@WithMockUser("email")
	void test_createTask_whenProjectNotFound_shouldReturnNotFound() throws JsonProcessingException, Exception {
		doThrow(new AppProjectNotFoundException("test message")).when(authorize).check(anyString(), any());
		NewTaskDTOIn taskDto = new NewTaskDTOIn("test name", "description","11");
		
		mvc.perform(post(FIXTURE_URL)
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(taskDto)))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$",is("test message")));
		
		verify(authorize).check("email", taskDto);
		verifyNoInteractions(createTask);
	}
	
	@Test
	void test_createProject_withoutAuthentication_shouldReturnUnauthorized() throws JsonProcessingException, Exception {
		mvc.perform(post(FIXTURE_URL)
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new NewTaskDTOIn("task name", "description", "1"))))
			.andExpect(status().isUnauthorized());
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(createTask);
	}
	
	@Test
	@WithMockUser
	void test_createProject_withoutCsrfToken_shouldReturnForbidden() throws JsonProcessingException, Exception {
		mvc.perform(post(FIXTURE_URL)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new NewTaskDTOIn("task name", "description", "1"))))
			.andExpect(status().isForbidden());
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(createTask);
	}
	
	@Test
	@WithMockUser("mock@user.it")
	void test_createTask_whenProjectIsMissing_shouldReturnNotFound() throws JsonProcessingException, Exception {
		doThrow(new AppProjectNotFoundException("test message")).when(authorize).check(any(), any());
		
		mvc.perform(post(FIXTURE_URL)
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new NewTaskDTOIn("task name", "description", "1"))))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$", is("test message")));
		
		verify(authorize).check("mock@user.it", new NewTaskDTOIn("task name", "description", "1"));
		verifyNoInteractions(createTask);
	}
}
