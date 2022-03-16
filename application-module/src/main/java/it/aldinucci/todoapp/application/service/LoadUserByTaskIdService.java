package it.aldinucci.todoapp.application.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.LoadUserByTaskIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadUserByTaskIdDriverPort;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exception.AppTaskNotFoundException;

@Service
@Transactional
public class LoadUserByTaskIdService implements LoadUserByTaskIdUsePort{

	private LoadUserByTaskIdDriverPort loadUser;
	
	@Autowired
	public LoadUserByTaskIdService(LoadUserByTaskIdDriverPort loadUser) {
		this.loadUser = loadUser;
	}

	@Override
	public User load(TaskIdDTO taskId) throws AppTaskNotFoundException {
		return loadUser.load(taskId.taskId()); 
	}

}
