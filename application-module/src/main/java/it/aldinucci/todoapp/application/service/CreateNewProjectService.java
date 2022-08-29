package it.aldinucci.todoapp.application.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.CreateProjectUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewProjectDTOIn;
import it.aldinucci.todoapp.application.port.out.CreateProjectDriverPort;
import it.aldinucci.todoapp.application.port.out.IsUserPresentDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.NewProjectData;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Service
@Transactional
class CreateNewProjectService implements CreateProjectUsePort {

	private final CreateProjectDriverPort newProjectDriverPort;
	private final IsUserPresentDriverPort isUserPresent;
	private final AppGenericMapper<NewProjectDTOIn, NewProjectData> mapper;

	@Autowired
	public CreateNewProjectService(CreateProjectDriverPort newProjectDriverPort,
			IsUserPresentDriverPort isUserPresent,
			AppGenericMapper<NewProjectDTOIn, NewProjectData> mapper) {
		this.newProjectDriverPort = newProjectDriverPort;
		this.isUserPresent = isUserPresent;
		this.mapper = mapper;
	}

	@Override
	public Project create(NewProjectDTOIn projectDto) throws AppUserNotFoundException {
		if(!isUserPresent.isPresent(projectDto.getUserEmail()))
				throw new AppUserNotFoundException("User not found with email: " + projectDto.getUserEmail());
		
		return newProjectDriverPort.create(mapper.map(projectDto));
	}

}
