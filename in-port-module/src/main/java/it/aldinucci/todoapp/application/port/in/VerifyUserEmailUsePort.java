package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.VerifyTokenDTOIn;

public interface VerifyUserEmailUsePort {
	
	public boolean verify(VerifyTokenDTOIn token);
}
