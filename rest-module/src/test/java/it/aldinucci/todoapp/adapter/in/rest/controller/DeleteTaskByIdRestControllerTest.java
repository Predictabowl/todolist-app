package it.aldinucci.todoapp.adapter.in.rest.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.validation.ConstraintViolationException;

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

import it.aldinucci.todoapp.adapter.in.rest.security.config.AppRestSecurityConfig;
import it.aldinucci.todoapp.application.port.in.DeleteTaskByIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@WebMvcTest(controllers = {DeleteTaskByIdRestController.class})
@ExtendWith(SpringExtension.class)
@Import(AppRestSecurityConfig.class)
class DeleteTaskByIdRestControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private InputModelAuthorization<TaskIdDTO> authorize;
	
	@MockBean
	private DeleteTaskByIdUsePort deleteTask;
	
	
	@Test
	@WithMockUser("user@email.org")
	void test_deleteTask_successful() throws Exception {
		
		mvc.perform(delete("/api/task/6")
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
		
		InOrder inOrder = Mockito.inOrder(authorize,deleteTask);
		TaskIdDTO model = new TaskIdDTO(6);
		inOrder.verify(authorize).check("user@email.org", model);
		inOrder.verify(deleteTask).delete(model);
	}
	
	@Test
	void test_deleteTask_withoutAuthentication_shouldReturnUnathorized() throws Exception {
		
		mvc.perform(delete("/api/task/1")
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(deleteTask);
	}
	
	@Test
	@WithMockUser
	void test_deleteTask_withoutCsrfToken_shouldReturnForbidden() throws Exception {
		mvc.perform(post("/api/task/2")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isForbidden());
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(deleteTask);
	}
}
