package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.NewUserDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.NewUserDtoOut;
import it.aldinucci.todoapp.exception.AppEmailAlreadyRegisteredException;

public interface CreateUserUsePort {
	
	public NewUserDtoOut create(NewUserDTOIn newUser) throws AppEmailAlreadyRegisteredException;
}
