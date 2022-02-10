package it.aldinucci.todoapp.application.port.out;

import it.aldinucci.todoapp.domain.Project;

public interface LoadProjectPort {
	
	public Project loadProject(Long projectId);
}
