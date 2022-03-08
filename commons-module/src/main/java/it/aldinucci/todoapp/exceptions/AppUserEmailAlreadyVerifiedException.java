package it.aldinucci.todoapp.exceptions;

public class AppUserEmailAlreadyVerifiedException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AppUserEmailAlreadyVerifiedException(String message) {
		super(message);
	}

}
