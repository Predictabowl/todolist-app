package it.aldinucci.todoapp.webcommons.security.authorization;


import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.webcommons.exception.ForbiddenWebAccessException;

public interface InputModelAuthorization<T> {
	
	public void check(UserIdDTO id, T model) throws ForbiddenWebAccessException;
}
