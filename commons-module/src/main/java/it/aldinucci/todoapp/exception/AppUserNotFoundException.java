package it.aldinucci.todoapp.exception;

public class AppUserNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AppUserNotFoundException(String message) {
		super(message);
	}

}
