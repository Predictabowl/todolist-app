package it.aldinucci.todoapp.adapter.in.rest.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import it.aldinucci.todoapp.adapter.in.rest.dto.NewProjectRestDto;
import it.aldinucci.todoapp.application.port.in.CreateProjectUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewProjectDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;
import it.aldinucci.todoapp.webcommons.security.config.AppRestSecurityConfig;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {CreateProjectRestController.class})
@Import({AppRestSecurityConfig.class})
class CreateProjectRestControllerTest {

	private static final String FIXTURE_URL = "/api/project/create";
	
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
	@WithMockUser("user@email.it")
	void test_createProject_successful() throws JsonProcessingException, Exception {
		Project project = new Project(2L, "test project");
		NewProjectRestDto restDto = new NewProjectRestDto("another name");
		when(createPort.create(isA(NewProjectDTOIn.class)))
			.thenReturn(project);
		
		mvc.perform(post(FIXTURE_URL)
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(restDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(2)))
			.andExpect(jsonPath("$.name", is("test project")));
		
		verify(createPort).create(new NewProjectDTOIn("another name", "user@email.it"));
		verifyNoMoreInteractions(createPort);
	}
	
	@Test
	@WithMockUser
	void test_createProject_withEmptyName_shouldSendBadRequest() throws JsonProcessingException, Exception {
		
		mvc.perform(post(FIXTURE_URL)
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new NewProjectRestDto(""))))
			.andExpect(status().isBadRequest());
		
		verifyNoInteractions(createPort);
	}
	
	@Test
	@WithMockUser("test@email.it")
	void test_createProject_whenUserNotFound_shouldReturnBadRequest() throws JsonProcessingException, Exception {
		when(createPort.create(isA(NewProjectDTOIn.class)))
			.thenThrow(new AppUserNotFoundException("test message"));
		
		mvc.perform(post(FIXTURE_URL)
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new NewProjectRestDto("test name"))))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$",is("test message")));
		
		verify(createPort).create(new NewProjectDTOIn("test name", "test@email.it"));
	}
	
	@Test
	void test_createProject_withoutAuthentication_shouldReturnUnauthorized() throws JsonProcessingException, Exception {
		mvc.perform(post(FIXTURE_URL)
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new NewProjectRestDto("test name"))))
			.andExpect(status().isUnauthorized());
		
		verifyNoInteractions(createPort);
	}
	
	@Test
	@WithMockUser
	void test_createProjet_withoutCsrfToken_shouldReturnForbidden() throws JsonProcessingException, Exception {
		mvc.perform(post(FIXTURE_URL)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new NewTaskDTOIn("task name", "description", 1))))
			.andExpect(status().isForbidden());
		
		verifyNoInteractions(createPort);
	}

}
