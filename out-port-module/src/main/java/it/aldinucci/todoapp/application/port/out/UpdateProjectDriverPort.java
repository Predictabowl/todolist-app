package it.aldinucci.todoapp.application.port.out;

import java.util.Optional;

import it.aldinucci.todoapp.domain.Project;

public interface UpdateProjectDriverPort {

	public Optional<Project> update(Project project);
}
