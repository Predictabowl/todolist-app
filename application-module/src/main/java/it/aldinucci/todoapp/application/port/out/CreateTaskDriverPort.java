package it.aldinucci.todoapp.application.port.out;

import it.aldinucci.todoapp.application.port.out.dto.NewTaskDTOOut;
import it.aldinucci.todoapp.domain.Task;

public interface CreateTaskDriverPort {

	public Task create(NewTaskDTOOut task);
}
