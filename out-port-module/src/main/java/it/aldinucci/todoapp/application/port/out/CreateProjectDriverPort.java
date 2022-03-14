package it.aldinucci.todoapp.application.port.out;

import it.aldinucci.todoapp.application.port.out.dto.NewProjectData;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;

public interface CreateProjectDriverPort {
	
	public Project create(NewProjectData project) throws AppUserNotFoundException;
}
