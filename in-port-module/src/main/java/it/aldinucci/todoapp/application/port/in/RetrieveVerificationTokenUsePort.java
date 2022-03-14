package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exception.AppUserEmailAlreadyVerifiedException;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;

public interface RetrieveVerificationTokenUsePort {

	public VerificationToken get(UserIdDTO userId)
			throws AppUserNotFoundException, AppUserEmailAlreadyVerifiedException;
}
