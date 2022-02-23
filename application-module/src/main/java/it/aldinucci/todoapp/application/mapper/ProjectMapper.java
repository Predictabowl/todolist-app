package it.aldinucci.todoapp.application.mapper;

import it.aldinucci.todoapp.application.port.in.dto.NewProjectDTOIn;
import it.aldinucci.todoapp.application.port.out.dto.NewProjectDTOOut;
import it.aldinucci.todoapp.domain.Project;

public interface ProjectMapper {

	public NewProjectDTOOut map(NewProjectDTOIn newProject);
}
