package it.aldinucci.todoapp.adapter.in.rest.controller;


import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import it.aldinucci.todoapp.adapter.in.rest.security.config.AppRestSecurityConfig;
import it.aldinucci.todoapp.application.port.in.UpdateProjectUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectDataDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.mapper.AppGenericMapper;
import it.aldinucci.todoapp.webcommons.dto.ProjectDataWebDto;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@WebMvcTest(controllers = UpdateProjectRestController.class)
@ExtendWith(SpringExtension.class)
@Import(AppRestSecurityConfig.class)
class UpdateProjectRestControllerTest {

	private static final String USER_EMAIL_FIXTURE = "user@email.it";

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private InputModelAuthorization<ProjectIdDTO> authorize;
	
	@MockBean
	private UpdateProjectUsePort updateProject;
	
	@MockBean
	private AppGenericMapper<ProjectDataWebDto, ProjectDataDTOIn> mapper;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Test
	void test_updateProject_withoutAuthentication_shouldReturnUnauthorized() throws Exception {
		
		mvc.perform(put("/api/project/3")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(updateProject);
		verifyNoInteractions(mapper);
	}

	@Test
	@WithMockUser(USER_EMAIL_FIXTURE)
	void test_updateProject_withoutCSRF_shouldReturnForbidden() throws Exception {
		
		mvc.perform(put("/api/project/3")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isForbidden());
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(updateProject);
		verifyNoInteractions(mapper);
	}
	
	/**
	 * For this situation to happen authorization on the ProjectId should be granted,
	 * which means that this could only happen in case of data integrity failure or 
	 * because interleaving.
	 * @throws Exception
	 */
	@Test
	@WithMockUser(USER_EMAIL_FIXTURE)
	void test_updateProject_whenProjectNotFound_shouldReturnBadRequest() throws Exception {
		when(updateProject.update(any(), any())).thenReturn(Optional.empty());
		ProjectDataDTOIn dtoIn = new ProjectDataDTOIn("new name");
		when(mapper.map(any())).thenReturn(dtoIn);
		
		mvc.perform(put("/api/project/3")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new ProjectDataWebDto("new name"))))
			.andExpect(status().isBadRequest());
		
		InOrder inOrder = Mockito.inOrder(authorize, updateProject, mapper);
		ProjectIdDTO projectIdDTO = new ProjectIdDTO("3");
		inOrder.verify(authorize).check(new UserIdDTO(USER_EMAIL_FIXTURE), projectIdDTO);
		inOrder.verify(mapper).map(new ProjectDataWebDto("new name"));
		inOrder.verify(updateProject).update(projectIdDTO, dtoIn);
	}
	
	@Test
	@WithMockUser(USER_EMAIL_FIXTURE)
	void test_updateProject_whenDataIsNotValid_shouldReturnBadRequest() throws Exception {
		when(updateProject.update(any(), any())).thenReturn(Optional.empty());
		
		mvc.perform(put("/api/project/3")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new ProjectDataWebDto(""))))
			.andExpect(status().isBadRequest());
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(updateProject);
		verifyNoInteractions(mapper);
	}
	
	@Test
	@WithMockUser(USER_EMAIL_FIXTURE)
	void test_updateProject_success() throws Exception {
		Project project = new Project("3", "different name");
		when(updateProject.update(any(), any())).thenReturn(Optional.of(project));
		ProjectDataDTOIn dtoIn = new ProjectDataDTOIn("different name");
		when(mapper.map(any())).thenReturn(dtoIn);
		
		mvc.perform(put("/api/project/3")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(project)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is("3")))
			.andExpect(jsonPath("$.name", is("different name")));
		
		InOrder inOrder = Mockito.inOrder(authorize, updateProject, mapper);
		ProjectIdDTO projectIdDTO = new ProjectIdDTO("3");
		inOrder.verify(authorize).check(new UserIdDTO(USER_EMAIL_FIXTURE), projectIdDTO);
		inOrder.verify(mapper).map(new ProjectDataWebDto("different name"));
		inOrder.verify(updateProject).update(projectIdDTO, dtoIn);
	}
}
