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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import it.aldinucci.todoapp.application.port.in.CreateTaskUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@WebMvcTest(controllers = {CreateTaskWebController.class})
@ExtendWith(SpringExtension.class)
class CreateTaskWebControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private InputModelAuthorization<ProjectIdDTO> authorize;
	
	@MockBean
	private CreateTaskUsePort createTask;
	
	@Test
	@WithMockUser("user@email.it")
	void test_createNewTask() throws Exception {
		
		mvc.perform(post("/project/3/task/new")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.param("name", "task name")
				.param("description", "task descr"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/project/3/tasks"));
		
		InOrder inOrder = Mockito.inOrder(authorize, createTask);
		inOrder.verify(authorize).check("user@email.it", new ProjectIdDTO(3));
		inOrder.verify(createTask).create(new NewTaskDTOIn("task name", "task descr", 3));
	}
	
	@Test
	@WithMockUser("user@email.it")
	void test_createNewTask_csrfCheck() throws Exception {
		
		mvc.perform(post("/project/3/task/new")
				.contentType(MediaType.APPLICATION_JSON)
				.param("name", "task name")
				.param("description", "task descr"))
			.andExpect(status().isForbidden());
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(createTask);
	}
	
	@Test
	@WithMockUser("user@email.it")
	void test_createNewTask_whenInputValidationFail_shouldRedirect() throws Exception {
		
		mvc.perform(post("/project/3/task/new")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.param("name", "")
				.param("description", "task descr"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/project/3/tasks"));

		verifyNoInteractions(authorize);
		verifyNoInteractions(createTask);
	}
	
}
