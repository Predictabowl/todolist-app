package it.aldinucci.todoapp.adapter.in.rest.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.aldinucci.todoapp.adapter.in.rest.security.config.AppRestSecurityConfig;
import it.aldinucci.todoapp.application.port.in.DeleteProjectByIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.webcommons.handler.AppWebExceptionHandlers;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@WebMvcTest(controllers = {DeleteProjectByIdRestController.class})
@Import({AppRestSecurityConfig.class, AppWebExceptionHandlers.class})
class DeleteProjectByIdRestControllerTest {

	private static final String EMAIL_FIXTURE = "mock@user.it";

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private InputModelAuthorization<ProjectIdDTO> authorize;
	
	@MockBean
	private DeleteProjectByIdUsePort deleteProject;
	
	
	@Test
	@WithMockUser(EMAIL_FIXTURE)
	void test_deleteProject_successful() throws Exception {
		when(deleteProject.delete(any())).thenReturn(true);
		
		mvc.perform(delete("/api/project/5")
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
		
		InOrder inOrder = Mockito.inOrder(authorize,deleteProject);
		ProjectIdDTO model = new ProjectIdDTO("5");
		inOrder.verify(authorize).check(new UserIdDTO(EMAIL_FIXTURE), model);
		inOrder.verify(deleteProject).delete(model);
	}

	@Test
	void test_deleteProject_withoutAuthentication_shouldReturnUnathorized() throws Exception {
		
		mvc.perform(delete("/api/project/5")
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(deleteProject);
	}
	
	@Test
	@WithMockUser
	void test_deleteProject_withoutCsrfToken_shouldReturnForbidden() throws Exception {
		mvc.perform(post("/api/project/3")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isForbidden());
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(deleteProject);
	}
	
	@Test
	@WithMockUser(EMAIL_FIXTURE)
	void test_deleteProject_whenProjectIsMissing_shouldReturnNotFound() throws JsonProcessingException, Exception {
		when(deleteProject.delete(any())).thenReturn(false);
		
		mvc.perform(delete("/api/project/3")
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
		
		verify(authorize).check(new UserIdDTO(EMAIL_FIXTURE), new ProjectIdDTO("3"));
		verify(deleteProject).delete(new ProjectIdDTO("3"));
	}
}
