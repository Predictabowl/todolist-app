package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.StringTokenDTOIn;

public interface VerifyResetPasswordTokenUsePort {

	public boolean verify(StringTokenDTOIn token);
}
