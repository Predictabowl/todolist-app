package it.aldinucci.todoapp.adapter.in.web.controller;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import it.aldinucci.todoapp.application.port.in.DeleteProjectByIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@WebMvcTest(controllers = {DeleteProjectWebController.class})
class DeleteProjectWebControllerTest {
	
	private static final String FIXTURE_TEST_URL = "/web/project/";
	private static final String FIXTURE_USER_EMAIL = "user@email.it";
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private InputModelAuthorization<ProjectIdDTO> authorize;
	
	@MockBean
	private DeleteProjectByIdUsePort deleteProject;
	
	@Test
	void test_deleteProject_whenUserNotAuthenticated() throws Exception {
		
		mvc.perform(delete(FIXTURE_TEST_URL+"7")
			.with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("http://localhost/login"));
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(deleteProject);
	}

	@Test
	@WithMockUser(FIXTURE_USER_EMAIL)
	void test_deleteProject_withoutCsrfToken() throws Exception {
		
		mvc.perform(delete(FIXTURE_TEST_URL+"7"))
				.andExpect(status().isForbidden());
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(deleteProject);
	}
	
	@Test
	@WithMockUser(FIXTURE_USER_EMAIL)
	void test_deleteProject_success() throws Exception {
		
		mvc.perform(delete(FIXTURE_TEST_URL+"7")
				.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/web"));
		
		ProjectIdDTO idDTO = new ProjectIdDTO("7");
		verify(authorize).check(new UserIdDTO(FIXTURE_USER_EMAIL), idDTO);
		verify(deleteProject).delete(idDTO);
	}
	
}
