package it.aldinucci.todoapp.application.port.out;

import java.util.Optional;

import it.aldinucci.todoapp.domain.Task;

public interface UpdateTaskDriverPort {

	public Optional<Task> update(Task task);
}
