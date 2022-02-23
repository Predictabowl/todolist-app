package it.aldinucci.todoapp.application.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.LoadUserFromProjectIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadUserByProjectIdDriverPort;
import it.aldinucci.todoapp.domain.User;

@Service
@Transactional
public class LoadUserFromProjectIdService implements LoadUserFromProjectIdUsePort{

	private LoadUserByProjectIdDriverPort loadUser;
	
	@Autowired
	public LoadUserFromProjectIdService(LoadUserByProjectIdDriverPort loadUser) {
		this.loadUser = loadUser;
	}

	@Override
	public User load(ProjectIdDTO projectId) {
		return loadUser.load(projectId.getProjectId());
	}

}
