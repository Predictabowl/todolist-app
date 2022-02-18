package it.aldinucci.todoapp.application.port.out;

import it.aldinucci.todoapp.domain.Project;

public interface UpdateProjectByIdDriverPort {

	public boolean update(Project project);
}
