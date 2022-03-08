package it.aldinucci.todoapp.application.mapper;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.in.dto.NewProjectDTOIn;
import it.aldinucci.todoapp.application.port.out.dto.NewProjectData;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class ProjectMapperInOut implements AppGenericMapper<NewProjectDTOIn, NewProjectData>{

	@Override
	public NewProjectData map(NewProjectDTOIn model) {
		return new NewProjectData(model.getName(), model.getUserEmail());
	}
	
}
