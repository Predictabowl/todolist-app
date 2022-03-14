package it.aldinucci.todoapp.webcommons.security.authorization;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import it.aldinucci.todoapp.application.port.in.LoadUserByTaskIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exception.AppTaskNotFoundException;
import it.aldinucci.todoapp.webcommons.exception.UnauthorizedWebAccessException;

class TaskIdWebAuthorizationTest {

	@Mock
	private LoadUserByTaskIdUsePort loadUser;
	
	@InjectMocks
	private TaskIdWebAuthorization authorize;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
	}
	
	@Test
	void test_authorizationSuccessful() throws AppTaskNotFoundException {
		User user = new User("email", "username", "password");
		TaskIdDTO model = new TaskIdDTO(3L);
		when(loadUser.load(isA(TaskIdDTO.class))).thenReturn(user);
		
		assertThatCode(() -> {
			authorize.check("email", model);
		})
			.doesNotThrowAnyException();
		
		verify(loadUser).load(model);
	}
	
	@Test
	void test_authorizationFailure_shouldThrow() throws AppTaskNotFoundException {
		User user = new User("email", "username", "password");
		TaskIdDTO taskId = new TaskIdDTO(3L);
		when(loadUser.load(isA(TaskIdDTO.class))).thenReturn(user);
		
		assertThatThrownBy(() -> authorize.check("different email", taskId))
			.isInstanceOf(UnauthorizedWebAccessException.class)
			.hasMessage("This operation is not permitted for the authenticated user");
		
		verify(loadUser).load(taskId);
	}

}
