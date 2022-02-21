package it.aldinucci.todoapp.exceptions;

public class ProjectPersistenceException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProjectPersistenceException(String message) {
		super(message);
	}
}
