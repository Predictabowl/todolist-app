package it.aldinucci.todoapp.adapter.in.rest.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

import it.aldinucci.todoapp.adapter.in.rest.security.config.AppRestSecurityConfig;
import it.aldinucci.todoapp.application.port.in.ToggleTaskCompleteStatusUsePort;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.exception.AppTaskNotFoundException;
import it.aldinucci.todoapp.webcommons.handler.AppWebExceptionHandlers;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@WebMvcTest(controllers = ToggleTaskCompleteStatusRestController.class)
@Import({AppRestSecurityConfig.class, AppWebExceptionHandlers.class})
class ToggleTaskCompleteStatusRestControllerTest {

	private static final String USER_EMAIL_FIXTURE = "user@email.it";

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private ToggleTaskCompleteStatusUsePort togglePort;
	
	@MockBean
	private InputModelAuthorization<TaskIdDTO> authorize;
	
	@Test
	void test_toggleTask_withoutAuthentication_shouldReturnUnauthorized() throws Exception {
		
		mvc.perform(put("/api/task/1/completed/toggle")
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(togglePort);
	}
	
	@Test
	@WithMockUser(USER_EMAIL_FIXTURE)
	void test_toggleTask_withoutCSRF_shouldReturnForbidden() throws Exception {
		
		mvc.perform(put("/api/task/1/completed/toggle")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isForbidden());
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(togglePort);
	}
	
	@Test
	@WithMockUser(USER_EMAIL_FIXTURE)
	void test_toggleStatus_success() throws Exception {

		mvc.perform(put("/api/task/3/completed/toggle")
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
		
		InOrder inOrder = Mockito.inOrder(authorize, togglePort);
		TaskIdDTO idDTO = new TaskIdDTO("3");
		inOrder.verify(authorize).check(new UserIdDTO(USER_EMAIL_FIXTURE), idDTO);
		inOrder.verify(togglePort).toggle(idDTO);
	}
	
	@Test
	@WithMockUser(USER_EMAIL_FIXTURE)
	void test_toggleStatus_whenTaskNotFound_shouldReturnNotFound() throws Exception {
		doThrow(new AppTaskNotFoundException("test message")).when(authorize)
			.check(isA(UserIdDTO.class), any());
		
		mvc.perform(put("/api/task/5/completed/toggle")
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$", is("test message")));
		
		verify(authorize).check(new UserIdDTO(USER_EMAIL_FIXTURE), new TaskIdDTO("5"));
		verifyNoInteractions(togglePort);
	}
	

}
