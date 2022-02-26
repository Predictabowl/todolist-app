package it.aldinucci.todoapp.webcommons.exception;

public class UnauthorizaedWebAccessException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnauthorizaedWebAccessException() {
		super();
	}
	
	public UnauthorizaedWebAccessException(String message) {
		super(message);
	}
}
