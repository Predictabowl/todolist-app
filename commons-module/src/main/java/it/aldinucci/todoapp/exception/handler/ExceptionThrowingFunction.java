package it.aldinucci.todoapp.exception.handler;


@FunctionalInterface
public interface ExceptionThrowingFunction<T, R, E extends Exception> {

	public R apply(T t) throws E;
	
}
