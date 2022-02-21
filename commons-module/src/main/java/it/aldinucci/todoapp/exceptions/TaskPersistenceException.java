package it.aldinucci.todoapp.exceptions;

public class TaskPersistenceException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TaskPersistenceException(String message) {
		super(message);
	}

}
