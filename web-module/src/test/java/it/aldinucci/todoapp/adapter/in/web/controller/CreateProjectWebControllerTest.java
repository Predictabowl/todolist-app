package it.aldinucci.todoapp.adapter.in.web.controller;

import static org.mockito.Mockito.verify;
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
		NewProjectDTOIn newProjectDTOIn = new NewProjectDTOIn("new project", "user@email.it");

		mvc.perform(post("/project/new")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.param("name", "new project"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/"));
		
		verify(createProject).create(newProjectDTOIn);
	}
	
	@Test
	void test_createProject_withoutAuthorization_shouldRedirect() throws Exception {
		mvc.perform(post("/project/new")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.param("name", "new project"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/login"));
	}

}
