package it.aldinucci.todoapp.webcommons.security.authorization;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.webcommons.exception.ForbiddenWebAccessException;

@Component
public class AuthenticatedUserWebAuthorization implements InputModelAuthorization<User>{

	@Override
	public void check(UserIdDTO userId, User user) throws ForbiddenWebAccessException {
		if(!userId.getId().equals(user.getEmail()))
				throw new ForbiddenWebAccessException("This operation is not permitted for the authenticated user");
	}

}
