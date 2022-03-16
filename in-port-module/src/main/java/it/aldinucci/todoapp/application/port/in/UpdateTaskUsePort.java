package it.aldinucci.todoapp.application.port.in;

import java.util.Optional;

import it.aldinucci.todoapp.application.port.in.dto.TaskDataDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.domain.Task;

public interface UpdateTaskUsePort {

	public Optional<Task> update(TaskIdDTO taskId, TaskDataDTOIn taskData);
}
