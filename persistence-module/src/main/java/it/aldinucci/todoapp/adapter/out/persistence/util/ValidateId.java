package it.aldinucci.todoapp.adapter.out.persistence.util;

public interface ValidateId<T> {

	public boolean isValid(String id);
	
	public T getId();
}
