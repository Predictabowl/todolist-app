package it.aldinucci.todoapp.adapter.in.web.controller;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import it.aldinucci.todoapp.application.port.in.CreateProjectUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewProjectDTOIn;
import it.aldinucci.todoapp.domain.Project;

@WebMvcTest(controllers = {CreateProjectWebController.class})
@ExtendWith(SpringExtension.class)
class CreateProjectWebControllerTest {

	@MockBean
	private CreateProjectUsePort createProject;
	
	@Autowired
	private MockMvc mvc;
	
	@Test
	@WithMockUser("user@email.it")
	void test_createProject_success() throws Exception {
		when(createProject.create(isA(NewProjectDTOIn.class))).thenReturn(new Project("4", "Project name"));
		NewProjectDTOIn newProjectDTOIn = new NewProjectDTOIn("new project", "user@email.it");

		mvc.perform(post("/web/project/new")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.param("name", "new project"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/web/project/4/tasks"));
		
		verify(createProject).create(newProjectDTOIn);
	}
	
	@Test
	void test_createProject_withoutAuthorization_shouldRedirect() throws Exception {
		mvc.perform(post("/web/project/new")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.param("name", "new project"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/login"));
		
		verifyNoInteractions(createProject);
	}
	
	@Test
	@WithMockUser("user@email.it")
	void test_createProject_withEmptyName_shouldRedirect() throws Exception {
		mvc.perform(post("/web/project/new")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.param("name", ""))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/web"));
		
		verifyNoInteractions(createProject);
	}

}
