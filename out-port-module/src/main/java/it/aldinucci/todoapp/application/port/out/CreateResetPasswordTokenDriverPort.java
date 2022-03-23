package it.aldinucci.todoapp.application.port.out;

import it.aldinucci.todoapp.application.port.out.dto.ResetPasswordTokenData;
import it.aldinucci.todoapp.domain.ResetPasswordToken;
import it.aldinucci.todoapp.exception.AppResetPasswordTokenAlreadyExistsException;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;

public interface CreateResetPasswordTokenDriverPort {

	public ResetPasswordToken create(ResetPasswordTokenData tokenData)
		throws AppResetPasswordTokenAlreadyExistsException, AppUserNotFoundException;
}
