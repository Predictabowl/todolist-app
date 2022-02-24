package it.aldinucci.todoapp.webcommons.security.custom;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadUserByProjectIdDriverPort;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.webcommons.exception.ForbiddenWebAccessException;
import it.aldinucci.todoapp.webcommons.security.authorization.ProjectIdWebAuthorization;

class ProjectIdWebAuthorizationTest {

	@Mock
	private LoadUserByProjectIdDriverPort loadUser;
	
	@InjectMocks
	private ProjectIdWebAuthorization authorize;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
	}
	
	@Test
	void test_authorizationSuccessful() {
		User user = new User("email", "username", "password");
		when(loadUser.load(anyLong())).thenReturn(user);
		
		assertThatCode(() -> authorize.check("email", new ProjectIdDTO(3L)))
			.doesNotThrowAnyException();
		
		verify(loadUser).load(3L);
	}
	
	@Test
	void test_authorizationFailure_shouldThrow() {
		User user = new User("email", "username", "password");
		ProjectIdDTO projectId = new ProjectIdDTO(3L);
		when(loadUser.load(anyLong())).thenReturn(user);
		
		assertThatThrownBy(() -> authorize.check("different email", projectId))
			.isInstanceOf(ForbiddenWebAccessException.class)
			.hasMessage("This operation is not permitted for the authenticated user");
		
		verify(loadUser).load(3L);
	}

}
