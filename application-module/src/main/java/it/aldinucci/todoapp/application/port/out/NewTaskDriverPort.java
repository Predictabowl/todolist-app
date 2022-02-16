package it.aldinucci.todoapp.application.port.out;

import it.aldinucci.todoapp.domain.Task;

public interface NewTaskDriverPort {

	public Task save(Task task);
}
