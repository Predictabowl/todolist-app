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

import it.aldinucci.todoapp.application.port.in.LoadUserByProjectIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;
import it.aldinucci.todoapp.webcommons.exception.UnauthorizedWebAccessException;

class NewTaskWebAuthorizationTest {

	@Mock
	private LoadUserByProjectIdUsePort loadUser;
	
	@InjectMocks
	private NewTaskWebAuthorization authorize;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
	}
	
	@Test
	void test_authorizeSuccess() throws AppProjectNotFoundException {
		NewTaskDTOIn newTask = new NewTaskDTOIn("task name", "descr", 3L);
		User user = new User("email1", "username", "password");
		when(loadUser.load(isA(ProjectIdDTO.class))).thenReturn(user);
		
		assertThatCode(() -> authorize.check("email1", newTask))
			.doesNotThrowAnyException();
		
		verify(loadUser).load(new ProjectIdDTO(3L));
	}

	@Test
	void test_authorizeWhenProjectIdDoesNotBelongToAuthenticateUser_shouldThrow() throws AppProjectNotFoundException {
		NewTaskDTOIn newTask = new NewTaskDTOIn("task name", "descr", 3L);
		User user = new User("another email", "username", "password");
		when(loadUser.load(isA(ProjectIdDTO.class))).thenReturn(user);
		
		assertThatThrownBy(() -> authorize.check("email1", newTask))
			.isInstanceOf(UnauthorizedWebAccessException.class)
			.hasMessage("Operation not authorized for the autheticated user");
		
		verify(loadUser).load(new ProjectIdDTO(3L));
	}
}
