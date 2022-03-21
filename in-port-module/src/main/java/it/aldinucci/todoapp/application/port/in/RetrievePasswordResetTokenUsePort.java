package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.ResetPasswordToken;

public interface RetrievePasswordResetTokenUsePort {
	
	public ResetPasswordToken get(UserIdDTO userId);
}
