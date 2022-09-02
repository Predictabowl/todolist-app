package it.aldinucci.todoapp.adapter.in.rest.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

import com.fasterxml.jackson.core.JsonProcessingException;

import it.aldinucci.todoapp.adapter.in.rest.security.config.AppRestSecurityConfig;
import it.aldinucci.todoapp.application.port.in.DeleteTaskByIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.exception.AppTaskNotFoundException;
import it.aldinucci.todoapp.webcommons.handler.AppWebExceptionHandlers;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@WebMvcTest(controllers = {DeleteTaskByIdRestController.class})
@ExtendWith(SpringExtension.class)
@Import({AppRestSecurityConfig.class, AppWebExceptionHandlers.class})
class DeleteTaskByIdRestControllerTest {

	private static final String USER_EMAIL_FIXTURE = "user@email.org";

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private InputModelAuthorization<TaskIdDTO> authorize;
	
	@MockBean
	private DeleteTaskByIdUsePort deleteTask;
	
	
	@Test
	@WithMockUser(USER_EMAIL_FIXTURE)
	void test_deleteTask_successful() throws Exception {
		when(deleteTask.delete(any())).thenReturn(true);
		
		mvc.perform(delete("/api/task/6")
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
		
		InOrder inOrder = Mockito.inOrder(authorize,deleteTask);
		TaskIdDTO model = new TaskIdDTO("6");
		inOrder.verify(authorize).check(new UserIdDTO(USER_EMAIL_FIXTURE), model);
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
		mvc.perform(delete("/api/task/2")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isForbidden());
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(deleteTask);
	}
	
	@Test
	@WithMockUser(USER_EMAIL_FIXTURE)
	void test_deleteTask_whenTaskIsMissing_shouldReturnNotFound() throws JsonProcessingException, Exception {
		doThrow(new AppTaskNotFoundException("test message")).when(authorize).check(any(), any());
		
		mvc.perform(delete("/api/task/3")
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$", is("test message")));
		
		verify(authorize).check(new UserIdDTO(USER_EMAIL_FIXTURE), new TaskIdDTO("3"));
		verifyNoInteractions(deleteTask);
	}
	
	@Test
	@WithMockUser(USER_EMAIL_FIXTURE)
	void test_deleteTask_whenCannotDelete_shouldReturnNotFound() throws JsonProcessingException, Exception {
		when(deleteTask.delete(any())).thenReturn(false);
		
		mvc.perform(delete("/api/task/3")
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
		
		verify(authorize).check(new UserIdDTO(USER_EMAIL_FIXTURE), new TaskIdDTO("3"));
		verify(deleteTask).delete(new TaskIdDTO("3"));
	}
}
