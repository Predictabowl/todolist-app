package it.aldinucci.todoapp.webcommons.security.authorization;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.webcommons.exception.ForbiddenWebAccessException;

class AuthenticatedUserWebAuthorizationTest {
	
	private AuthenticatedUserWebAuthorization authorize;
	
	@BeforeEach
	void setUp() {
		authorize = new AuthenticatedUserWebAuthorization();
	}
	
	@Test
	void test_authorizeUserMatch() {
		User user = new User("auth@email.it", "username", "password");
		UserIdDTO userId = new UserIdDTO("auth@email.it");
		
		assertThatCode(() -> authorize.check(userId, user))
			.doesNotThrowAnyException();
	}
	
	@Test
	void test_authorizationFailure_shouldThrow() {
		User user = new User("user@email.it", "username", "password");
		UserIdDTO userId = new UserIdDTO("auth@email.it");
		
		assertThatThrownBy(() -> authorize.check(userId, user))
			.isInstanceOf(ForbiddenWebAccessException.class)
			.hasMessage("This operation is not permitted for the authenticated user");
	}
}
