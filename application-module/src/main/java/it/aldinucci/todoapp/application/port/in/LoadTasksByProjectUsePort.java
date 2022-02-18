package it.aldinucci.todoapp.application.port.in;

import java.util.List;

import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.domain.Task;

public interface LoadTasksByProjectUsePort {
	
	
	public List<Task> load(ProjectIdDTO projectId);
	
}
