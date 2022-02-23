package it.aldinucci.todoapp.application.mapper;

import it.aldinucci.todoapp.application.port.in.dto.NewProjectDTOIn;
import it.aldinucci.todoapp.application.port.out.dto.NewProjectDTOOut;

public class ProjectMapperImpl implements ProjectMapper {

	@Override
	public NewProjectDTOOut map(NewProjectDTOIn newProject) {
		return new NewProjectDTOOut(newProject.getName(), newProject.getUserEmail());
	}

}
