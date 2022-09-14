package it.aldinucci.todoapp.webcommons.security.authorization;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import it.aldinucci.todoapp.application.port.in.LoadUserByTaskIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exception.AppTaskNotFoundException;
import it.aldinucci.todoapp.webcommons.exception.ForbiddenWebAccessException;

class TaskIdWebAuthorizationTest {

	private static final String EMAIL_FIXTURE = "email@test.it";
	
	@Mock
	private LoadUserByTaskIdUsePort loadUser;
	
	@InjectMocks
	private TaskIdWebAuthorization authorize;

	private AutoCloseable closeable;
	
	@BeforeEach
	void setUp() {
		closeable = openMocks(this);
	}
	
	@AfterEach
	void tearDown() throws Exception {
		closeable.close();
	}
	
	@Test
	void test_authorizationSuccessful() {
		User user = new User(EMAIL_FIXTURE, "username", "password");
		TaskIdDTO model = new TaskIdDTO("3");
		UserIdDTO userId = new UserIdDTO(EMAIL_FIXTURE);
		when(loadUser.load(isA(TaskIdDTO.class))).thenReturn(Optional.of(user));
		
		assertThatCode(() -> {
			authorize.check(userId, model);
		})
			.doesNotThrowAnyException();
		
		verify(loadUser).load(model);
	}
	
	@Test
	void test_authorizationFailure_shouldThrow() {
		User user = new User(EMAIL_FIXTURE, "username", "password");
		TaskIdDTO taskId = new TaskIdDTO("3");
		UserIdDTO userId = new UserIdDTO("different@email.com");
		when(loadUser.load(isA(TaskIdDTO.class))).thenReturn(Optional.of(user));
		
		assertThatThrownBy(() -> authorize.check(userId, taskId))
			.isInstanceOf(ForbiddenWebAccessException.class)
			.hasMessage("This operation is not permitted for the authenticated user");
		
		verify(loadUser).load(taskId);
	}
	
	@Test
	void test_authorizationWhenCannotFindTask_shouldThrow() {
		TaskIdDTO taskId = new TaskIdDTO("3");
		UserIdDTO userId = new UserIdDTO("different@email.com");
		when(loadUser.load(isA(TaskIdDTO.class))).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> authorize.check(userId, taskId))
			.isInstanceOf(AppTaskNotFoundException.class)
			.hasMessage("Could not find Task with id: 3");
		
		verify(loadUser).load(taskId);
	}

}
