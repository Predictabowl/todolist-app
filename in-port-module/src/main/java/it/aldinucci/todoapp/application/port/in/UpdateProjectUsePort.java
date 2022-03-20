package it.aldinucci.todoapp.application.port.in;

import java.util.Optional;

import it.aldinucci.todoapp.application.port.in.dto.ProjectDataDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.domain.Project;

public interface UpdateProjectUsePort {

	public Optional<Project> update(ProjectIdDTO projectId, ProjectDataDTOIn data);
}
