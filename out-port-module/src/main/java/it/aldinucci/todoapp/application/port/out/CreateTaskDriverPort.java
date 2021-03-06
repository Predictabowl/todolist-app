package it.aldinucci.todoapp.application.port.out;

import it.aldinucci.todoapp.application.port.out.dto.NewTaskData;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;

public interface CreateTaskDriverPort {

	public Task create(NewTaskData task) throws AppProjectNotFoundException;
}
