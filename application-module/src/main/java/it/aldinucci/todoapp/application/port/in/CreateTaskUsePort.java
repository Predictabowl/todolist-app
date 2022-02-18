package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.domain.Task;

public interface CreateTaskUsePort {

	public Task create(NewTaskDTOIn task);
}
