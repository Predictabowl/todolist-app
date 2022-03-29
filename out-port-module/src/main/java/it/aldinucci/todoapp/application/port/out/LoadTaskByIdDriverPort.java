package it.aldinucci.todoapp.application.port.out;

import java.util.Optional;

import it.aldinucci.todoapp.domain.Task;

public interface LoadTaskByIdDriverPort {

	public Optional<Task> load(String id);
}
