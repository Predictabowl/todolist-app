package it.aldinucci.todoapp.application.port.out;

import it.aldinucci.todoapp.application.port.out.dto.ResetPasswordTokenData;
import it.aldinucci.todoapp.domain.ResetPasswordToken;

public interface CreateResetPasswordTokenDriverPort {

	public ResetPasswordToken create(ResetPasswordTokenData tokenData);
}
