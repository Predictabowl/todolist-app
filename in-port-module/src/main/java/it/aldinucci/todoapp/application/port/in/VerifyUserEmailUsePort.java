package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.StringTokenDTOIn;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;

public interface VerifyUserEmailUsePort {
	
	public boolean verify(StringTokenDTOIn token) throws AppUserNotFoundException;
}
