package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.VerifyTokenDTOIn;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;

public interface VerifyUserEmailUsePort {
	
	public boolean verify(VerifyTokenDTOIn token) throws AppUserNotFoundException;
}
