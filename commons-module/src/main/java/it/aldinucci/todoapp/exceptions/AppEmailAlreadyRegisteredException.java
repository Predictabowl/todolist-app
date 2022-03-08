package it.aldinucci.todoapp.exceptions;

public class AppEmailAlreadyRegisteredException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AppEmailAlreadyRegisteredException(String message) {
		super(message);
	}

}
