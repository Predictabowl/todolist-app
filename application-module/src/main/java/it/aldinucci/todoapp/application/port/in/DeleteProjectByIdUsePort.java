package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;

public interface DeleteProjectByIdUsePort {

	public void delete(ProjectIdDTO id);
}
