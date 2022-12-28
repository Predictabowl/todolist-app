package it.aldinucci.todoapp.adapter.in.web.controller;


import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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

import it.aldinucci.todoapp.application.port.in.CreateTaskUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.webcommons.exception.ForbiddenWebAccessException;
import it.aldinucci.todoapp.webcommons.handler.AppWebExceptionHandlers;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@WebMvcTest(controllers = {CreateTaskWebController.class})
@Import(AppWebExceptionHandlers.class)
class CreateTaskWebControllerTest {

	private static final String USER_EMAIL_FIXTURE = "user@email.it";

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private InputModelAuthorization<ProjectIdDTO> authorize;
	
	@MockBean
	private CreateTaskUsePort createTask;
	
	@Test
	@WithMockUser(USER_EMAIL_FIXTURE)
	void test_createNewTask() throws Exception {
		
		mvc.perform(post("/web/project/3/task/new")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.param("name", "task name")
				.param("description", "task descr"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/web/project/3/tasks"));
		
		InOrder inOrder = Mockito.inOrder(authorize, createTask);
		inOrder.verify(authorize).check(new UserIdDTO(USER_EMAIL_FIXTURE), new ProjectIdDTO("3"));
		inOrder.verify(createTask).create(new NewTaskDTOIn("task name", "task descr", "3"));
	}
	
	@Test
	@WithMockUser(USER_EMAIL_FIXTURE)
	void test_createNewTask_csrfCheck() throws Exception {
		
		mvc.perform(post("/web/project/3P/task/new")
				.contentType(MediaType.APPLICATION_JSON)
				.param("name", "task name")
				.param("description", "task descr"))
			.andExpect(status().isForbidden());
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(createTask);
	}
	
	@Test
	@WithMockUser(USER_EMAIL_FIXTURE)
	void test_createNewTask_whenInputValidationFail_shouldRedirect() throws Exception {
		doThrow(ForbiddenWebAccessException.class)
			.when(authorize).check(isA(UserIdDTO.class), isA(ProjectIdDTO.class));
		
		mvc.perform(post("/web/project/3/task/new")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.param("name", "")
				.param("description", "task descr"))
			.andExpect(status().is4xxClientError());

		verify(authorize).check(new UserIdDTO(USER_EMAIL_FIXTURE), new ProjectIdDTO("3"));
		verifyNoInteractions(createTask);
	}
	
}
