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
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.webcommons.exception.ForbiddenWebAccessException;

class ProjectIdWebAuthorizationTest {

	@Mock
	private LoadUserByProjectIdUsePort loadUser;
	
	@InjectMocks
	private ProjectIdWebAuthorization authorize;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
	}
	
	@Test
	void test_authorizationSuccessful() {
		User user = new User("email", "username", "password");
		ProjectIdDTO model = new ProjectIdDTO(3L);
		when(loadUser.load(isA(ProjectIdDTO.class))).thenReturn(user);
		
		assertThatCode(() -> {
			authorize.check("email", model);
		})
			.doesNotThrowAnyException();
		
		verify(loadUser).load(model);
	}
	
	@Test
	void test_authorizationFailure_shouldThrow() {
		User user = new User("email", "username", "password");
		ProjectIdDTO projectId = new ProjectIdDTO(3L);
		when(loadUser.load(isA(ProjectIdDTO.class))).thenReturn(user);
		
		assertThatThrownBy(() -> authorize.check("different email", projectId))
			.isInstanceOf(ForbiddenWebAccessException.class)
			.hasMessage("This operation is not permitted for the authenticated user");
		
		verify(loadUser).load(projectId);
	}

}