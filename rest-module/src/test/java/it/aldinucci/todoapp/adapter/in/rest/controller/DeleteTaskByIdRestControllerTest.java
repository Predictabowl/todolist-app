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

import it.aldinucci.todoapp.application.port.in.DeleteTaskByIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.exceptions.AppTaskNotFoundException;
import it.aldinucci.todoapp.webcommons.config.security.AppRestSecurityConfig;
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
		
		mvc.perform(delete(BASE_REST_URI+"/task/6")
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
		
		InOrder inOrder = Mockito.inOrder(authorize,deleteTask);
		TaskIdDTO model = new TaskIdDTO(6);
		inOrder.verify(authorize).check("user@email.org", model);
		inOrder.verify(deleteTask).delete(model);
	}

	@Test
	@WithMockUser("user")
	void test_deleteTask_whenNotPresent_shouldReturnNotFound() throws Exception {
		AppTaskNotFoundException exception = new AppTaskNotFoundException("no task");
		doThrow(exception).when(deleteTask).delete(isA(TaskIdDTO.class));
		
		mvc.perform(delete(BASE_REST_URI+"/task/5")
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
		
		InOrder inOrder = Mockito.inOrder(authorize,deleteTask);
		TaskIdDTO model = new TaskIdDTO(5L);
		inOrder.verify(authorize).check("user", model);
		inOrder.verify(deleteTask).delete(model);
	}
}
