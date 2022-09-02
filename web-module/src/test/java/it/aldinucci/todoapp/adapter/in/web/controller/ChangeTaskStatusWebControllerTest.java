package it.aldinucci.todoapp.adapter.in.web.controller;


import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import it.aldinucci.todoapp.application.port.in.ToggleTaskCompleteStatusUsePort;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@WebMvcTest(controllers = {ChangeTaskStatusWebController.class})
@ExtendWith(SpringExtension.class)
class ChangeTaskStatusWebControllerTest {

	private static final String USER_EMAIL_FIXTURE = "user@email.it";

	@MockBean
	private ToggleTaskCompleteStatusUsePort toggleStatus;
	
	@MockBean
	private InputModelAuthorization<TaskIdDTO> authorize;
	
	@Autowired
	private MockMvc mvc;
	
	@Test
	void test_changeStatus_withoutAuthentication_shouldRedirect() throws Exception {
		mvc.perform(post("/web/project/5/task/2/toggle/completed")
				.with(csrf())
				.with(request -> {
						request.setServerName("somewhere");
						return request;
					}))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://somewhere/login"));
		
		verifyNoInteractions(toggleStatus);
		verifyNoInteractions(authorize);
	}
	
	@Test
	@WithMockUser(USER_EMAIL_FIXTURE)
	void test_changeStatus_success() throws Exception {
		mvc.perform(post("/web/project/5/task/2/toggle/completed")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/web/project/5/tasks"));
		
		InOrder inOrder = Mockito.inOrder(toggleStatus, authorize);
		TaskIdDTO idDTO = new TaskIdDTO("2");
		inOrder.verify(authorize).check(new UserIdDTO(USER_EMAIL_FIXTURE), idDTO);
		inOrder.verify(toggleStatus).toggle(idDTO);
	}
	

}
