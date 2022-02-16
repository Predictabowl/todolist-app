package it.aldinucci.todoapp.application.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.NewTaskUsePort;
import it.aldinucci.todoapp.application.port.out.NewTaskDriverPort;
import it.aldinucci.todoapp.domain.Task;

@Service
@Transactional
class CreateNewTaskService implements NewTaskUsePort{

	private final NewTaskDriverPort newTaskPort;
	
	@Autowired
	public CreateNewTaskService(NewTaskDriverPort newTaskPort) {
		this.newTaskPort = newTaskPort;
	}

	@Override
	public Task create(Task task) {
		return newTaskPort.save(task);
	}
}
