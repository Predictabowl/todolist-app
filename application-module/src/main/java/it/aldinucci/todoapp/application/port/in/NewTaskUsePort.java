package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.domain.Task;

public interface NewTaskUsePort {

	public Task create(Task task);
}
