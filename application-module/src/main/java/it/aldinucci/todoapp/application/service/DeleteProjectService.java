package it.aldinucci.todoapp.application.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.DeleteProjectByIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.out.DeleteProjectByIdDriverPort;
import it.aldinucci.todoapp.exceptions.AppProjectNotFoundException;

@Service
@Transactional
public class DeleteProjectService implements DeleteProjectByIdUsePort{

	private final DeleteProjectByIdDriverPort deleteProjectPort;
	
	@Autowired
	public DeleteProjectService(DeleteProjectByIdDriverPort deleteProjectPort) {
		this.deleteProjectPort = deleteProjectPort;
	}

	@Override
	public void delete(ProjectIdDTO id) throws AppProjectNotFoundException {
		deleteProjectPort.delete(id.getProjectId());
	}

}
