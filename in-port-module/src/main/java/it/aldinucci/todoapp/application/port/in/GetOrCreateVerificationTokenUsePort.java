package it.aldinucci.todoapp.application.port.in;

import java.util.Optional;

import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exception.AppUserEmailAlreadyVerifiedException;

public interface GetOrCreateVerificationTokenUsePort {

	/**
	 * Get an existing valid verification token for the user,
	 * otherwise creates a new one.
	 * 
	 * Returns an empy optional if the user is not found.
	 * 
	 * @param userId
	 * @return A valid token or and empty optional if the user is not found
	 * @throws AppUserEmailAlreadyVerifiedException if the user is already enabled
	 */
	public Optional<VerificationToken> get(UserIdDTO userId)
			throws AppUserEmailAlreadyVerifiedException;
}
