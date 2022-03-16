package it.aldinucci.todoapp.adapter.in.rest.controller;


import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import it.aldinucci.todoapp.adapter.in.rest.security.config.AppRestSecurityConfig;
import it.aldinucci.todoapp.application.port.in.UpdateTaskUsePort;
import it.aldinucci.todoapp.application.port.in.dto.TaskDataDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.webcommons.dto.TaskDataWebDto;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@WebMvcTest(controllers = UpdateTaskRestController.class)
@ExtendWith(SpringExtension.class)
@Import(AppRestSecurityConfig.class)
class UpdateTaskRestControllerTest {

	private static final String FIXTURE_TEST_URL = "/api/task/3";

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private InputModelAuthorization<TaskIdDTO> authorize;
	
	@MockBean
	private UpdateTaskUsePort updateTask;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Test
	void test_updateTask_withoutAuthentication_shouldReturnUnauthorized() throws Exception {
		
		mvc.perform(put(FIXTURE_TEST_URL)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isUnauthorized());
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(updateTask);
	}

	@Test
	@WithMockUser("user@email.it")
	void test_updateTask_withoutCSRF_shouldReturnForbidden() throws Exception {
		
		mvc.perform(put(FIXTURE_TEST_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isForbidden());
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(updateTask);
	}
	
	@Test
	@WithMockUser("user@email.it")
	void test_updateTask_whenTaskNotFound_shouldReturnBadRequest() throws Exception {
		when(updateTask.update(any(), any())).thenReturn(Optional.empty());
		
		mvc.perform(put(FIXTURE_TEST_URL)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new TaskDataWebDto("new name", "new descr"))))
			.andExpect(status().isBadRequest());
		
		InOrder inOrder = Mockito.inOrder(authorize, updateTask);
		TaskIdDTO taskIdDTO = new TaskIdDTO(3);
		inOrder.verify(authorize).check("user@email.it", taskIdDTO);
		inOrder.verify(updateTask).update(taskIdDTO, new TaskDataDTOIn("new name", "new descr"));
	}
	
	@Test
	@WithMockUser("user@email.it")
	void test_updateTask_whenDataIsNotValid_shouldReturnBadRequest() throws Exception {
		when(updateTask.update(any(), any())).thenReturn(Optional.empty());
		mvc.perform(put(FIXTURE_TEST_URL)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new TaskDataWebDto("",""))))
			.andExpect(status().isBadRequest());
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(updateTask);
	}
	
	@Test
	@WithMockUser("user@email.it")
	void test_updateTask_success() throws Exception {
		Task task = new Task(3L, "name", "description", false, 7);
		when(updateTask.update(any(), any())).thenReturn(Optional.of(task));
		
		mvc.perform(put(FIXTURE_TEST_URL)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(task)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(3)))
			.andExpect(jsonPath("$.name", is("name")))
			.andExpect(jsonPath("$.description", is("description")))
			.andExpect(jsonPath("$.completed", is(false)))
			.andExpect(jsonPath("$.orderInProject", is(7)));
		
		InOrder inOrder = Mockito.inOrder(authorize, updateTask);
		TaskIdDTO taskIdDTO = new TaskIdDTO(3);
		inOrder.verify(authorize).check("user@email.it", taskIdDTO);
		inOrder.verify(updateTask).update(taskIdDTO, new TaskDataDTOIn("name", "description"));
	}

}
