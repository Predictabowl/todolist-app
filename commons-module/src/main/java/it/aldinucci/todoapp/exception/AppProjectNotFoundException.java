package it.aldinucci.todoapp.exception;

public class AppProjectNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AppProjectNotFoundException(String message) {
		super(message);
	}
}
