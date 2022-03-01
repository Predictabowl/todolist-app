package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.NewUserDTOIn;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exceptions.AppEmailAlreadyRegisteredException;

public interface CreateUserUsePort {
	
	public User create(NewUserDTOIn newUser) throws AppEmailAlreadyRegisteredException;
}
