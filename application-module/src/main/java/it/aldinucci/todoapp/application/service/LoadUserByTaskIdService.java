package it.aldinucci.todoapp.application.service;

import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.LoadUserByTaskIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.domain.User;

@Service
public class LoadUserByTaskIdService implements LoadUserByTaskIdUsePort{

	@Override
	public User load(TaskIdDTO taskId) {
		throw new UnsupportedOperationException("Method not implemented yet");
	}

}
