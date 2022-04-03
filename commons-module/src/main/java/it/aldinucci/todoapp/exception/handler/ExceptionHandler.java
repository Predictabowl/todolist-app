package it.aldinucci.todoapp.exception.handler;

public interface ExceptionHandler<T, R, E extends Exception> {

	public R doItWithHandler(ExceptionThrowingFunction<T, R, E> code);
	
}
