package it.aldinucci.todoapp.application.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.LoadUserByProjectIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadUserByProjectIdDriverPort;
import it.aldinucci.todoapp.domain.User;

@Service
@Transactional
public class LoadUserByProjectIdService implements LoadUserByProjectIdUsePort{

	private LoadUserByProjectIdDriverPort loadUser;
	
	@Autowired
	public LoadUserByProjectIdService(LoadUserByProjectIdDriverPort loadUser) {
		this.loadUser = loadUser;
	}

	@Override
	public User load(ProjectIdDTO projectId) {
		return loadUser.load(projectId.getProjectId());
	}

}
