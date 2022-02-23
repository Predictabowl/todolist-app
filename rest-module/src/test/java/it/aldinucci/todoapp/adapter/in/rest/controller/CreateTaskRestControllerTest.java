package it.aldinucci.todoapp.adapter.in.rest.controller;


import static it.aldinucci.todoapp.webcommons.config.AppBaseUrls.BASE_REST_URL;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.aldinucci.todoapp.application.port.in.CreateTaskUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exceptions.AppProjectNotFoundException;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {CreateTaskRestController.class})
@AutoConfigureMockMvc(addFilters = false)
class CreateTaskRestControllerTest {
	
	private static final String FIXTURE_URL = BASE_REST_URL+"/task/create";
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private CreateTaskUsePort createTask;
	
	private ObjectMapper objectMapper;
	
	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
	}
	

	@Test
	void test_createTask_successful() throws JsonProcessingException, Exception {
		Task task = new Task(1L, "new task", "task description");
		NewTaskDTOIn taskDto = new NewTaskDTOIn("test name", "test description", 2L);
		when(createTask.create(isA(NewTaskDTOIn.class)))
			.thenReturn(task);
		
		mvc.perform(post(FIXTURE_URL)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(taskDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", is("new task")))
			.andExpect(jsonPath("$.description", is("task description")))
			.andExpect(jsonPath("$.id", is(1)));
		
		verify(createTask).create(taskDto);
		verifyNoMoreInteractions(createTask);
	}
	

	@Test
	void test_createTask_withEmptyName_shouldSendBadRequest() throws JsonProcessingException, Exception {
		NewTaskDTOIn taskDto = new NewTaskDTOIn("", "description",7L);
		
		mvc.perform(post(FIXTURE_URL)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(taskDto)))
			.andExpect(status().isBadRequest());
		
		verifyNoInteractions(createTask);
	}
	
	@Test
	void test_createTask_withNullId_shouldSendBadRequest() throws JsonProcessingException, Exception {
		NewTaskDTOIn taskDto = new NewTaskDTOIn("name", "description",null);
		
		mvc.perform(post(FIXTURE_URL)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(taskDto)))
			.andExpect(status().isBadRequest());
		
		verifyNoInteractions(createTask);
	}
	
	@Test
	void test_createTask_whenProjectNotFound_shouldReturnBadRequest() throws JsonProcessingException, Exception {
		NewTaskDTOIn taskDto = new NewTaskDTOIn("test name", "description",11L);
		when(createTask.create(isA(NewTaskDTOIn.class)))
			.thenThrow(new AppProjectNotFoundException("test message"));
		
		mvc.perform(post(FIXTURE_URL)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(taskDto)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$",is("test message")));
		
		verify(createTask).create(taskDto);
	}
}
