package it.aldinucci.todoapp.exception;

public class AppUserEmailAlreadyVerifiedException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AppUserEmailAlreadyVerifiedException(String message) {
		super(message);
	}

}
