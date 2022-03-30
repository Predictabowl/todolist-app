package it.aldinucci.todoapp.application.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.DeleteProjectByIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.out.DeleteProjectByIdDriverPort;

@Service
@Transactional
public class DeleteProjectService implements DeleteProjectByIdUsePort{

	private final DeleteProjectByIdDriverPort deleteProjectPort;
	
	@Autowired
	public DeleteProjectService(DeleteProjectByIdDriverPort deleteProjectPort) {
		this.deleteProjectPort = deleteProjectPort;
	}

	@Override
	public boolean delete(ProjectIdDTO id) {
		return deleteProjectPort.delete(id.getProjectId());
	}

}
