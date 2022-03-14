package it.aldinucci.todoapp.application.port.out;

import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exception.AppTaskNotFoundException;

public interface UpdateTaskDriverPort {

	public void update(Task task) throws AppTaskNotFoundException;
}
