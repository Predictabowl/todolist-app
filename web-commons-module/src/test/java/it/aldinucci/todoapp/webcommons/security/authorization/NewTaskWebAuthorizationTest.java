package it.aldinucci.todoapp.webcommons.security.authorization;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Optional;

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
	void test_authorizeSuccess(){
		NewTaskDTOIn newTask = new NewTaskDTOIn("task name", "descr", "3");
		User user = new User("email1", "username", "password");
		when(loadUser.load(isA(ProjectIdDTO.class))).thenReturn(Optional.of(user));
		
		assertThatCode(() -> authorize.check("email1", newTask))
			.doesNotThrowAnyException();
		
		verify(loadUser).load(new ProjectIdDTO("3"));
	}

	@Test
	void test_authorizeWhenProjectIdDoesNotBelongToAuthenticateUser_shouldThrow(){
		NewTaskDTOIn newTask = new NewTaskDTOIn("task name", "descr", "3");
		User user = new User("another email", "username", "password");
		when(loadUser.load(isA(ProjectIdDTO.class))).thenReturn(Optional.of(user));
		
		assertThatThrownBy(() -> authorize.check("email1", newTask))
			.isInstanceOf(UnauthorizedWebAccessException.class)
			.hasMessage("Operation not authorized for the autheticated user");
		
		verify(loadUser).load(new ProjectIdDTO("3"));
	}
	
	@Test
	void test_authorize_whenProjectNotFound_shouldThrow() {
		NewTaskDTOIn newTask = new NewTaskDTOIn("task name", "descr", "3");
		when(loadUser.load(isA(ProjectIdDTO.class))).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> authorize.check("email1", newTask))
			.isInstanceOf(AppProjectNotFoundException.class)
			.hasMessage("Could not find Project with id: 3");
		
		verify(loadUser).load(new ProjectIdDTO("3"));
	}
}
