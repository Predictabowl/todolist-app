package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.domain.Task;

public interface NewTaskPort {

	public Task save(Task task);
}
