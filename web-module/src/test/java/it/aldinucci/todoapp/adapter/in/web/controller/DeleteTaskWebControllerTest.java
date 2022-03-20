package it.aldinucci.todoapp.adapter.in.web.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import it.aldinucci.todoapp.application.port.in.DeleteTaskByIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@WebMvcTest(controllers = {DeleteTaskWebController.class})
@ExtendWith(SpringExtension.class)
class DeleteTaskWebControllerTest {

	private static final String FIXTURE_TEST_URL = "/web/project/1/task/";
	private static final String FIXTURE_EMAIL = "email@test.it";
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private InputModelAuthorization<TaskIdDTO> authorize;
	
	@MockBean
	private DeleteTaskByIdUsePort deleteTask;
	
	@Test
	void test_deleteTask_whenUserNotAuthenticated() throws Exception {
		
		mvc.perform(delete(FIXTURE_TEST_URL+"7")
			.with(csrf()))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("http://localhost/login"));
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(deleteTask);
	}

	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_deleteTask_withoutCsrfToken() throws Exception {
		
		mvc.perform(delete(FIXTURE_TEST_URL+"7"))
				.andExpect(status().isForbidden());
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(deleteTask);
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_deleteTask_withInvalidId() throws Exception {
		
		mvc.perform(delete(FIXTURE_TEST_URL+"2a")
				.with(csrf()))
				.andExpect(status().isBadRequest());
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(deleteTask);
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_deleteTask_success() throws Exception {
		
		mvc.perform(delete(FIXTURE_TEST_URL+"7")
				.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/web/project/1/tasks"));
		
		TaskIdDTO idDTO = new TaskIdDTO(7);
		verify(authorize).check(FIXTURE_EMAIL, idDTO);
		verify(deleteTask).delete(idDTO);
	}

}
