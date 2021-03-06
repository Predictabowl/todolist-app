package it.aldinucci.todoapp.webcommons.security.authorization;


import it.aldinucci.todoapp.webcommons.exception.UnauthorizedWebAccessException;

public interface InputModelAuthorization<T> {
	
	public void check(String authenticatedEmail, T model) throws UnauthorizedWebAccessException;
}
