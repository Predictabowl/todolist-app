package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.NewProjectDTOIn;
import it.aldinucci.todoapp.domain.Project;

public interface NewProjectUsePort {

	public Project create(NewProjectDTOIn projectDto);
}
