package it.aldinucci.todoapp.application.port.out;

import java.util.Optional;

import it.aldinucci.todoapp.domain.ResetPasswordToken;

public interface LoadResetPasswordTokenDriverPort {

	public Optional<ResetPasswordToken> load(String token);
}
