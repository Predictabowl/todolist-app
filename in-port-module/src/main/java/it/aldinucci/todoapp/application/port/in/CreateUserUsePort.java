package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.NewUserDTOIn;
import it.aldinucci.todoapp.domain.User;

public interface CreateUserUsePort {
	
	public User create(NewUserDTOIn newUser);
}
