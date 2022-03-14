package it.aldinucci.todoapp.exception;

public class AppEmailAlreadyRegisteredException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AppEmailAlreadyRegisteredException(String message) {
		super(message);
	}

}
