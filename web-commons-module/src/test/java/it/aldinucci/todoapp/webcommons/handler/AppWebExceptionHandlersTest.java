package it.aldinucci.todoapp.webcommons.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import it.aldinucci.todoapp.exception.AppProjectNotFoundException;
import it.aldinucci.todoapp.exception.AppTaskNotFoundException;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;
import it.aldinucci.todoapp.webcommons.exception.UnauthorizedWebAccessException;

@WebMvcTest(controllers = MockController.class)
@ExtendWith(SpringExtension.class)
@Import(AppWebExceptionHandlers.class)
@AutoConfigureMockMvc(addFilters = false)
class AppWebExceptionHandlersTest {

	private static final String TEST_URL = "/test/webcommon/controller";
	
	@MockBean
	private MockController mockController;
	
	@Autowired
	private MockMvc mvc;
	
	@Test
	void test_userNotFoundException() throws Exception {
		when(mockController.getToBeStubbed()).thenThrow(new AppUserNotFoundException("test message"));
		
		MockHttpServletResponse response = mvc.perform(get(TEST_URL))
			.andExpect(status().isNotFound())
			.andReturn().getResponse();
		
		assertThat(response.getContentAsString()).matches("test message");
	}
	
	@Test
	void test_projectNotFoundException() throws Exception {
		when(mockController.getToBeStubbed()).thenThrow(new AppProjectNotFoundException("test message"));
		
		MockHttpServletResponse response = mvc.perform(get(TEST_URL))
			.andExpect(status().isNotFound())
			.andReturn().getResponse();
		
		assertThat(response.getContentAsString()).matches("test message");
	}
	
	@Test
	void test_taskNotFoundException() throws Exception {
		when(mockController.getToBeStubbed()).thenThrow(new AppTaskNotFoundException("test message"));
		
		MockHttpServletResponse response = mvc.perform(get(TEST_URL))
			.andExpect(status().isNotFound())
			.andReturn().getResponse();
		
		assertThat(response.getContentAsString()).matches("test message");
	}
	
	@Test
	void test_unauthorizedWebException() throws Exception {
		when(mockController.getToBeStubbed()).thenThrow(new UnauthorizedWebAccessException("test message"));
		
		MockHttpServletResponse response = mvc.perform(get(TEST_URL))
			.andExpect(status().isUnauthorized())
			.andReturn().getResponse();
		
		assertThat(response.getContentAsString()).matches("test message");
	}
	
//	@Test
//	void test_constraintViolationWebException() throws Exception {
//		ConstraintViolationException exception = new ConstraintViolationException("test message",Collections.emptySet());
//		when(mockController.getToBeStubbed()).thenThrow(exception);
//		
//		MockHttpServletResponse response = mvc.perform(get(TEST_URL))
//			.andExpect(status().isBadRequest())
//			.andReturn().getResponse();
//		
//		assertThat(response.getContentAsString()).isEqualTo(exception.toString());
//	}

}
