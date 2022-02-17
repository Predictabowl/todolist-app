package it.aldinucci.todoapp.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.LoadAllUserProjectsUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadAllProjectsDriverPort;
import it.aldinucci.todoapp.domain.Project;

@Service
public class LoadAllProjectsService implements LoadAllUserProjectsUsePort{

	private final LoadAllProjectsDriverPort loadProjectsPort;
	
	@Autowired
	public LoadAllProjectsService(LoadAllProjectsDriverPort port) {
		this.loadProjectsPort = port;
	}
	
	@Override
	public List<Project> load(UserIdDTO userId) {
		return loadProjectsPort.load(userId.getEmail());
	}

}
