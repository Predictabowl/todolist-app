package it.aldinucci.todoapp.application.port.out;

import it.aldinucci.todoapp.application.port.out.dto.VerificationTokenData;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;

public interface CreateUserVerificationTokenDriverPort {

	public VerificationToken create(VerificationTokenData token)
			throws AppUserNotFoundException;
}
