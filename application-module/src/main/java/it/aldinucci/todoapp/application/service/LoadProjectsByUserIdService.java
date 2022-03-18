package it.aldinucci.todoapp.application.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.LoadProjectsByUserUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadProjectsByUserDriverPort;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;

@Service
@Transactional
public class LoadProjectsByUserIdService implements LoadProjectsByUserUsePort{

	private final LoadProjectsByUserDriverPort loadProjectsPort;
	
	@Autowired
	public LoadProjectsByUserIdService(LoadProjectsByUserDriverPort port) {
		this.loadProjectsPort = port;
	}
	
	@Override
	public List<Project> load(UserIdDTO userId) throws AppUserNotFoundException {
		return loadProjectsPort.load(userId.getEmail()).stream().sorted(
				(p1,p2) ->	p1.getName().compareTo(p2.getName()))
				.toList();
	}

}
