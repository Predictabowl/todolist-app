package it.aldinucci.todoapp.webcommons.dto.mapper;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.in.dto.ProjectDataDTOIn;
import it.aldinucci.todoapp.mapper.AppGenericMapper;
import it.aldinucci.todoapp.webcommons.dto.ProjectDataWebDto;

@Component
public class ProjectDataWebToInMapper implements AppGenericMapper<ProjectDataWebDto, ProjectDataDTOIn>{

	@Override
	public ProjectDataDTOIn map(ProjectDataWebDto model) {
		return new ProjectDataDTOIn(model.name());
	}

}
