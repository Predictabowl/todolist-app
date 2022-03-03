package it.aldinucci.todoapp.application.port.out;

import it.aldinucci.todoapp.application.port.out.dto.VerificationTokenDTOOut;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exceptions.AppUserNotFoundException;
import it.aldinucci.todoapp.exceptions.AppVerificationTokenAlreadyExistsException;

public interface CreateVerificationTokenDriverPort {

	public VerificationToken create(VerificationTokenDTOOut token)
			throws AppUserNotFoundException, AppVerificationTokenAlreadyExistsException;
}
