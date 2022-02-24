package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;

public interface DeleteTaskByIdUsePort {

	public void delete(TaskIdDTO task);
}
