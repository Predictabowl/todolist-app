package it.aldinucci.todoapp.webcommons.exception;

public class UnauthorizedWebAccessException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UnauthorizedWebAccessException(String message) {
		super(message);
	}
}
