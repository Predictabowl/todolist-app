package it.aldinucci.todoapp.application.port.in;

import java.util.Optional;

import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exceptions.AppUserNotFoundException;

public interface LoadVerificationTokenByEmailUsePort {
	
	public Optional<VerificationToken> load(UserIdDTO userId) throws AppUserNotFoundException;
}
