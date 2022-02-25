package it.aldinucci.todoapp.exceptions;

public class AppProjectNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AppProjectNotFoundException(String message) {
		super(message);
	}
}
