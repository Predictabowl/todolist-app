package it.aldinucci.todoapp.application.port.in;

import java.util.Optional;

import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.ResetPasswordToken;

public interface GetOrCreatePasswordResetTokenUsePort {
	
	public Optional<ResetPasswordToken> get(UserIdDTO userId);
}
