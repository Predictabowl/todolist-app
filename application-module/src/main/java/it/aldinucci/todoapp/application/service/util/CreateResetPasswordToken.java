package it.aldinucci.todoapp.application.service.util;

import it.aldinucci.todoapp.domain.ResetPasswordToken;
import it.aldinucci.todoapp.exception.AppResetPasswordTokenAlreadyExistsException;

public interface CreateResetPasswordToken {

	public ResetPasswordToken create(String email) throws AppResetPasswordTokenAlreadyExistsException;
}
