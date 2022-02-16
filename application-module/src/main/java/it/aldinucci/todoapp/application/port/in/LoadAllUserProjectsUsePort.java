package it.aldinucci.todoapp.application.port.in;

import java.util.List;

import it.aldinucci.todoapp.domain.Project;

public interface LoadAllUserProjectsUsePort {

	public List<Project> load(String userId);
}
