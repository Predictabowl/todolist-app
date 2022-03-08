package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.exceptions.AppProjectNotFoundException;

public interface DeleteProjectByIdUsePort {

	public void delete(ProjectIdDTO id) throws AppProjectNotFoundException;
}
