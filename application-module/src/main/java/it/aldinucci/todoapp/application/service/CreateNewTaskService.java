package it.aldinucci.todoapp.application.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.mapper.TaskMapper;
import it.aldinucci.todoapp.application.port.in.CreateTaskUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.application.port.out.CreateTaskDriverPort;
import it.aldinucci.todoapp.domain.Task;

@Service
@Transactional
class CreateNewTaskService implements CreateTaskUsePort{

	private final CreateTaskDriverPort newTaskPort;
	private final TaskMapper mapper;
	
	@Autowired
	public CreateNewTaskService(CreateTaskDriverPort newTaskPort, TaskMapper mapper) {
		this.newTaskPort = newTaskPort;
		this.mapper = mapper;
	}

	@Override
	public Task create(NewTaskDTOIn task) {
		return newTaskPort.create(mapper.map(task));
	}

}
