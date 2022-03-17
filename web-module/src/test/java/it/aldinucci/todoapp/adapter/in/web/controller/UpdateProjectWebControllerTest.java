package it.aldinucci.todoapp.adapter.in.web.controller;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.util.NestedServletException;

import it.aldinucci.todoapp.application.port.in.UpdateProjectUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectDataDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;
import it.aldinucci.todoapp.webcommons.dto.ProjectDataWebDto;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@WebMvcTest(controllers = UpdateProjectWebController.class)
@ExtendWith(SpringExtension.class)
class UpdateProjectWebControllerTest {

	private static final String BASE_URL = "/web/project";
	private static final String FIXTURE_EMAIL = "test@email.it";
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private InputModelAuthorization<ProjectIdDTO> authorize;
	
	@MockBean
	private AppGenericMapper<ProjectDataWebDto, ProjectDataDTOIn> mapper;
	
	@MockBean
	private UpdateProjectUsePort updateProject;
	
	@Test
	void test_updateWithoutAuth_shouldRedirectToLogin() throws Exception {
		mvc.perform(put(BASE_URL+"/2")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.param("name", "project name"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("http://localhost/login"));
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(updateProject);
		verifyNoInteractions(mapper);
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_updateWithourCsrf_shouldRturnForbidden() throws Exception {
		mvc.perform(put(BASE_URL+"/2")
			.contentType(MediaType.APPLICATION_JSON)
			.param("name", "project name"))
		.andExpect(status().isForbidden());
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(updateProject);
		verifyNoInteractions(mapper);
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_updateWhenProjectNotExists_shouldThrow() throws Exception {
		when(updateProject.update(any(), any())).thenReturn(Optional.empty());
		ProjectDataDTOIn dtoIn = new ProjectDataDTOIn("project name");
		when(mapper.map(any())).thenReturn(dtoIn);
		
		MockHttpServletRequestBuilder requestBuilder = put(BASE_URL+"/2")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.param("name", "project name");
		
		assertThatThrownBy(() ->  mvc.perform(requestBuilder))
			.isInstanceOf(NestedServletException.class)
			.getCause()
				.isInstanceOf(AppProjectNotFoundException.class)
				.hasMessage("Project not found with id: 2");
		
		InOrder inOrder = Mockito.inOrder(authorize, updateProject, mapper);
		inOrder.verify(authorize).check(FIXTURE_EMAIL, new ProjectIdDTO(2));
		inOrder.verify(mapper).map(new ProjectDataWebDto("project name"));
		inOrder.verify(updateProject).update(new ProjectIdDTO(2), dtoIn);
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_updateProject_withInvalidInput() throws Exception {
		
		mvc.perform(put(BASE_URL+"/2")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.param("name", ""))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:"+BASE_URL+"/2/tasks"));
		
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(updateProject);
		verifyNoInteractions(mapper);
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_updateProject_success() throws Exception {
		Project project = new Project(2L, "project name");
		when(updateProject.update(any(), any())).thenReturn(Optional.of(project));
		ProjectDataDTOIn dtoIn = new ProjectDataDTOIn("project name");
		when(mapper.map(any())).thenReturn(dtoIn);
		
		mvc.perform(put(BASE_URL+"/2")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.param("name", "project name"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:"+BASE_URL+"/2/tasks"));
		
		
		InOrder inOrder = Mockito.inOrder(authorize, updateProject, mapper);
		inOrder.verify(authorize).check(FIXTURE_EMAIL, new ProjectIdDTO(2));
		inOrder.verify(mapper).map(new ProjectDataWebDto("project name"));
		inOrder.verify(updateProject).update(new ProjectIdDTO(2), dtoIn);
	}

}
