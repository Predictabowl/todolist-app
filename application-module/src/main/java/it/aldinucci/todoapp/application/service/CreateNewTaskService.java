package it.aldinucci.todoapp.application.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.NewTaskPort;
import it.aldinucci.todoapp.domain.Task;

@Service
@Transactional
public class CreateNewTaskService {

	@Autowired
	private final NewTaskPort newTaskPort;
	
	public CreateNewTaskService(NewTaskPort newTaskPort) {
		this.newTaskPort = newTaskPort;
	}

	public Task create(Task task) {
		return newTaskPort.save(task);
	}
}
