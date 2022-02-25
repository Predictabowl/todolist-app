package it.aldinucci.todoapp.webcommons.exception;

public class ForbiddenWebAccessException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ForbiddenWebAccessException() {
		super();
	}
	
	public ForbiddenWebAccessException(String message) {
		super(message);
	}
}
