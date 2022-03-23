package it.aldinucci.todoapp.exception;

public class AppUserEmailAlreadyVerifiedException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AppUserEmailAlreadyVerifiedException(String message) {
		super(message);
	}

}
