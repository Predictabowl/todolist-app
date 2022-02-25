package it.aldinucci.todoapp.adapter.in.rest.controller;


import static it.aldinucci.todoapp.webcommons.config.AppBaseURIs.BASE_REST_URI;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import it.aldinucci.todoapp.application.port.in.DeleteProjectByIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.exceptions.AppProjectNotFoundException;
import it.aldinucci.todoapp.webcommons.config.security.AppRestSecurityConfig;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@WebMvcTest(controllers = {DeleteProjectByIdRestController.class})
@ExtendWith(SpringExtension.class)
@Import(AppRestSecurityConfig.class)
class DeleteProjectByIdRestControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private InputModelAuthorization<ProjectIdDTO> authorize;
	
	@MockBean
	private DeleteProjectByIdUsePort deleteProject;
	
	
	@Test
	@WithMockUser("user@email.org")
	void test_deleteProject_successful() throws Exception {
		
		mvc.perform(delete(BASE_REST_URI+"/project/5")
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
		
		InOrder inOrder = Mockito.inOrder(authorize,deleteProject);
		ProjectIdDTO model = new ProjectIdDTO(5L);
		inOrder.verify(authorize).check("user@email.org", model);
		inOrder.verify(deleteProject).delete(model);
	}

	@Test
	@WithMockUser("user")
	void test_deleteProject_whenProjectNotPresent_shouldReturnNotFound() throws Exception {
		AppProjectNotFoundException exception = new AppProjectNotFoundException("no project");
		doThrow(exception).when(deleteProject).delete(isA(ProjectIdDTO.class));
		
		mvc.perform(delete(BASE_REST_URI+"/project/5")
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
		
		InOrder inOrder = Mockito.inOrder(authorize,deleteProject);
		ProjectIdDTO model = new ProjectIdDTO(5L);
		inOrder.verify(authorize).check("user", model);
		inOrder.verify(deleteProject).delete(model);
	}
}
