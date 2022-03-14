package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;

public interface CreateTaskUsePort {

	public Task create(NewTaskDTOIn task) throws AppProjectNotFoundException;
}
