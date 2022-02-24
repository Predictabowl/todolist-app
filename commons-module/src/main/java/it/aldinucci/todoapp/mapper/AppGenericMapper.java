package it.aldinucci.todoapp.mapper;

public interface AppGenericMapper<T, R> {
	
	public R map(T model);
}
