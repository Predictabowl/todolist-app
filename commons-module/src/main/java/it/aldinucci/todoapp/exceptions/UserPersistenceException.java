package it.aldinucci.todoapp.exceptions;

public class UserPersistenceException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UserPersistenceException(String message) {
		super(message);
	}

}
