package it.aldinucci.todoapp.adapter.in.web.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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

import it.aldinucci.todoapp.application.port.in.UpdateTaskUsePort;
import it.aldinucci.todoapp.application.port.in.dto.TaskDataDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.exception.AppTaskNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;
import it.aldinucci.todoapp.webcommons.dto.TaskDataWebDto;
import it.aldinucci.todoapp.webcommons.exception.AppWebExceptionHandlers;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@WebMvcTest(controllers = {UpdateTaskWebController.class})
@ExtendWith(SpringExtension.class)
@Import({AppWebExceptionHandlers.class})
class UpdateTaskWebControllerTest {

	private static final String FIXTURE_EMAIL = "email@test.it";
	private static final String BASE_URL = "/web/project/1/task/";
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private InputModelAuthorization<TaskIdDTO> authorize;
	
	@MockBean
	private UpdateTaskUsePort updateTask;
	
	@MockBean
	private AppGenericMapper<TaskDataWebDto, TaskDataDTOIn> mapper;
	
	@Test
	void test_upateTask_withoutAuthShouldRedirect() throws Exception {
		mvc.perform(put(BASE_URL+"3")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.param("name", "new Name")
			.param("description", "new descr"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("http://localhost/login"));
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(updateTask);
		verifyNoInteractions(mapper);
	}

	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_upateTask_withoutCsrf_shouldReturnForbidden() throws Exception {
		mvc.perform(put(BASE_URL+"3")
			.contentType(MediaType.APPLICATION_JSON)
			.param("name", "new Name")
			.param("description", "new descr"))
		.andExpect(status().isForbidden());
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(updateTask);
		verifyNoInteractions(mapper);
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_upateTask_withInvalidInput_shouldRedirect() throws Exception {
		mvc.perform(put(BASE_URL+"3")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.param("name", "")
			.param("description", "new descr"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/web/project/1/tasks"));
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(updateTask);
		verifyNoInteractions(mapper);
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_upateTask_withInvalidProjectId() throws Exception {
		mvc.perform(put("/web/project/1a/task/3")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.param("name", "new name")
			.param("description", "new descr"))
		.andExpect(status().isBadRequest());
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(updateTask);
		verifyNoInteractions(mapper);
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_upateTask_withInvalidTaskId() throws Exception {
		mvc.perform(put("/web/project/1/task/3s")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.param("name", "new name")
			.param("description", "new descr"))
		.andExpect(status().isBadRequest());
		
		verifyNoInteractions(authorize);
		verifyNoInteractions(updateTask);
		verifyNoInteractions(mapper);
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_upateTask_whenTaskIdNotFound() throws Exception {
		doThrow(new AppTaskNotFoundException("test message")).when(authorize)
			.check(anyString(), any());
		
		mvc.perform(put("/web/project/1/task/3")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.param("name", "new name")
			.param("description", "new descr"))
		.andExpect(status().isNotFound());
		
		verify(authorize).check(FIXTURE_EMAIL, new TaskIdDTO(3));
		verifyNoInteractions(mapper);
		verifyNoInteractions(updateTask);
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_upateTask_success() throws Exception {
		TaskDataDTOIn dataDTOIn = new TaskDataDTOIn("test name", "test descr");
		when(mapper.map(any())).thenReturn(dataDTOIn);
		
		mvc.perform(put("/web/project/1/task/3")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.param("name", "new name")
			.param("description", "new descr"))
		.andExpect(status().is3xxRedirection())
		.andExpect(view().name("redirect:/web/project/1/tasks"));
		
		InOrder inOrder = Mockito.inOrder(authorize, mapper, updateTask);
		TaskIdDTO idDTO = new TaskIdDTO(3);
		inOrder.verify(authorize).check(FIXTURE_EMAIL, idDTO);
		inOrder.verify(mapper).map(new TaskDataWebDto("new name", "new descr"));
		inOrder.verify(updateTask).update(idDTO, dataDTOIn);
	}
}
