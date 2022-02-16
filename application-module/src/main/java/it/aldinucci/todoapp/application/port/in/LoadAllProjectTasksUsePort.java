package it.aldinucci.todoapp.application.port.in;

import java.util.List;

import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.domain.Task;

public interface LoadAllProjectTasksUsePort {

	public List<Task> getTasks(ProjectIdDTO projectId);
}
