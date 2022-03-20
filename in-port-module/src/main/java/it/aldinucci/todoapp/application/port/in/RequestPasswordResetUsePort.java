package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;

public interface RequestPasswordResetUsePort {
	
	public boolean send(UserIdDTO userId);
}
