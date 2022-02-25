package it.aldinucci.todoapp.exceptions;

public class AppTaskNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AppTaskNotFoundException(String message) {
		super(message);
	}

}
