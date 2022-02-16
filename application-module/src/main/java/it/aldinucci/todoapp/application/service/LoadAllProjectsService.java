package it.aldinucci.todoapp.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.aldinucci.todoapp.application.port.in.LoadAllUserProjectsUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadAllProjectsDriverPort;
import it.aldinucci.todoapp.domain.Project;


public class LoadAllProjectsService implements LoadAllUserProjectsUsePort{

	private LoadAllProjectsDriverPort port;
	
	@Autowired
	public LoadAllProjectsService(LoadAllProjectsDriverPort port) {
		this.port = port;
	}
	
	@Override
	public List<Project> load(UserIdDTO userId) {
		return port.load(userId.getEmail());
	}

}
