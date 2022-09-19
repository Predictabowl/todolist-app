package it.aldinucci.todoapp.adapter.out.persistence.util;

import java.util.Optional;

public interface ValidateId<T> {

	public Optional<T> getValidId(String id);
}
