package it.aldinucci.todoapp.application.mapper;

import it.aldinucci.todoapp.application.port.in.dto.NewProjectDTOIn;
import it.aldinucci.todoapp.application.port.out.dto.NewProjectDTOOut;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

public class ProjectMapperInOut implements AppGenericMapper<NewProjectDTOIn, NewProjectDTOOut>{

	@Override
	public NewProjectDTOOut map(NewProjectDTOIn model) {
		return new NewProjectDTOOut(model.getName(), model.getUserEmail());
	}
	
}
