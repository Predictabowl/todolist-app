package it.aldinucci.todoapp.application.service.util;

import it.aldinucci.todoapp.domain.ResetPasswordToken;

public interface CreateResetPasswordToken {

	public ResetPasswordToken create(String email);
}
