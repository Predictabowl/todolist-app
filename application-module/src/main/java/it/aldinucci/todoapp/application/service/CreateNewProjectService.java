package it.aldinucci.todoapp.application.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.CreateProjectUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewProjectDTOIn;
import it.aldinucci.todoapp.application.port.out.CreateProjectDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.NewProjectData;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Service
@Transactional
class CreateNewProjectService implements CreateProjectUsePort {

	private final CreateProjectDriverPort newProjectDriverPort;
	private final AppGenericMapper<NewProjectDTOIn, NewProjectData> mapper;

	public CreateNewProjectService(CreateProjectDriverPort newProjectDriverPort,
			AppGenericMapper<NewProjectDTOIn, NewProjectData> mapper) {
		this.newProjectDriverPort = newProjectDriverPort;
		this.mapper = mapper;
	}

	@Override
	public Project create(NewProjectDTOIn projectDto) throws AppUserNotFoundException {
		return newProjectDriverPort.create(mapper.map(projectDto));
	}

}
