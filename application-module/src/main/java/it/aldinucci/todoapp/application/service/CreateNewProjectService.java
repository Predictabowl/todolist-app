package it.aldinucci.todoapp.application.service;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.NewProjectDTOIn;
import it.aldinucci.todoapp.application.port.in.NewProjectUsePort;
import it.aldinucci.todoapp.application.port.out.NewProjectDTOOut;
import it.aldinucci.todoapp.application.port.out.NewProjectDriverPort;
import it.aldinucci.todoapp.domain.Project;

@Service
@Transactional
class CreateNewProjectService implements NewProjectUsePort {

	private NewProjectDriverPort newProjectDriverPort;

	private ModelMapper mapper;

	@Autowired
	public CreateNewProjectService(NewProjectDriverPort newProjectDriverPort, ModelMapper mapper) {
		this.newProjectDriverPort = newProjectDriverPort;
		this.mapper = mapper;
	}

	@Override
	public Project create(NewProjectDTOIn projectDto) {

		return newProjectDriverPort.create(mapper.map(projectDto, NewProjectDTOOut.class));
	}

}
