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

import it.aldinucci.todoapp.application.port.in.LoadUserByProjectIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;
import it.aldinucci.todoapp.webcommons.exception.ForbiddenWebAccessException;

class ProjectIdWebAuthorizationTest {

	private static final String EMAIL_FIXTURE = "email@test.it";
	
	@Mock
	private LoadUserByProjectIdUsePort loadUser;
	
	@InjectMocks
	private ProjectIdWebAuthorization authorize;

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
	void test_authorizationSuccessful(){
		User user = new User(EMAIL_FIXTURE, "username", "password");
		ProjectIdDTO model = new ProjectIdDTO("3");
		UserIdDTO userId = new UserIdDTO(EMAIL_FIXTURE);
		when(loadUser.load(isA(ProjectIdDTO.class))).thenReturn(Optional.of(user));
		
		assertThatCode(() -> {
			authorize.check(userId, model);
		})
			.doesNotThrowAnyException();
		
		verify(loadUser).load(model);
	}
	
	@Test
	void test_authorizationFailure_shouldThrow(){
		User user = new User(EMAIL_FIXTURE, "username", "password");
		ProjectIdDTO projectId = new ProjectIdDTO("3");
		UserIdDTO userId = new UserIdDTO("different@email.com");
		when(loadUser.load(isA(ProjectIdDTO.class))).thenReturn(Optional.of(user));
		
		assertThatThrownBy(() -> authorize.check(userId, projectId))
			.isInstanceOf(ForbiddenWebAccessException.class)
			.hasMessage("This operation is not permitted for the authenticated user");
		
		verify(loadUser).load(projectId);
	}
	
	@Test
	void test_authorizationWhenCantFindProject_shouldThrow() {
		ProjectIdDTO projectId = new ProjectIdDTO("3");
		UserIdDTO userId = new UserIdDTO("different@email.com");
		when(loadUser.load(isA(ProjectIdDTO.class))).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> authorize.check(userId, projectId))
			.isInstanceOf(AppProjectNotFoundException.class)
			.hasMessage("Could not find Project with id: 3");
		
		verify(loadUser).load(projectId);
	}

}
