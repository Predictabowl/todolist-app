package it.aldinucci.todoapp.adapter.in.rest.controller;

import static it.aldinucci.todoapp.adapter.in.rest.config.BaseRestUrl.BASE_REST_URL;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.aldinucci.todoapp.application.port.in.CreateProjectUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewProjectDTOIn;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.exceptions.UserNotFoundException;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {CreateProjectRestController.class})
class CreateProjectRestControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private CreateProjectUsePort createPort;
	
	private ObjectMapper objectMapper;
	
	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
	}
	
	@Test
	void test_createProject_successful() throws JsonProcessingException, Exception {
		Project project = new Project(2L, "test project");
		NewProjectDTOIn projectDto = new NewProjectDTOIn("another name", "user@email.it");
		when(createPort.create(isA(NewProjectDTOIn.class)))
			.thenReturn(project);
		
		mvc.perform(post(BASE_REST_URL+"/create")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(projectDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(2)))
			.andExpect(jsonPath("$.name", is("test project")));
		
		verify(createPort).create(projectDto);
		verifyNoMoreInteractions(createPort);
	}
	
	@Test
	void test_createProject_withInvalidEmailFormat_shouldSendBadRequest() throws JsonProcessingException, Exception {
		NewProjectDTOIn projectDto = new NewProjectDTOIn("another name", "user-email.it");
		
		mvc.perform(post(BASE_REST_URL+"/create")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(projectDto)))
			.andExpect(status().isBadRequest());
		
		verifyNoInteractions(createPort);
	}
	
	@Test
	void test_createProject_withEmptyName_shouldSendBadRequest() throws JsonProcessingException, Exception {
		NewProjectDTOIn projectDto = new NewProjectDTOIn("", "user@email.it");
		
		mvc.perform(post(BASE_REST_URL+"/create")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(projectDto)))
			.andExpect(status().isBadRequest());
		
		verifyNoInteractions(createPort);
	}
	
	@Test
	void test_createProject_whenUserNotFound_shouldReturnBadRequest() throws JsonProcessingException, Exception {
		NewProjectDTOIn projectDto = new NewProjectDTOIn("test name", "user@email.it");
		when(createPort.create(isA(NewProjectDTOIn.class)))
			.thenThrow(new UserNotFoundException("test message"));
		
		mvc.perform(post(BASE_REST_URL+"/create")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(projectDto)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$",is("test message")));
		
		verify(createPort).create(projectDto);
	}

}
