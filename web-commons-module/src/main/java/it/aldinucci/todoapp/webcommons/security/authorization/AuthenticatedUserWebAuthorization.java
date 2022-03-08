package it.aldinucci.todoapp.webcommons.security.authorization;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.webcommons.exception.UnauthorizedWebAccessException;

@Component
public class AuthenticatedUserWebAuthorization implements InputModelAuthorization<User>{

	@Override
	public void check(String authenticatedEmail, User user) throws UnauthorizedWebAccessException {
		if(!authenticatedEmail.equals(user.getEmail()))
				throw new UnauthorizedWebAccessException("This operation is not permitted for the authenticated user");
	}

}
