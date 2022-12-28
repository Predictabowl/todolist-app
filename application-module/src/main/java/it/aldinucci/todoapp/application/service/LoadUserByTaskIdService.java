package it.aldinucci.todoapp.application.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.LoadUserByTaskIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadUserByTaskIdDriverPort;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exception.AppTaskNotFoundException;

@Service
@Transactional
public class LoadUserByTaskIdService implements LoadUserByTaskIdUsePort{

	private final LoadUserByTaskIdDriverPort loadUser;
	
	public LoadUserByTaskIdService(LoadUserByTaskIdDriverPort loadUser) {
		this.loadUser = loadUser;
	}

	@Override
	public Optional<User> load(TaskIdDTO taskId) throws AppTaskNotFoundException {
		return loadUser.load(taskId.getTaskId()); 
	}

}
