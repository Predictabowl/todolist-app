package it.aldinucci.todoapp.application.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.CreateTaskUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.application.port.out.CreateTaskDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.NewTaskDTOOut;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exceptions.AppProjectNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Service
@Transactional
class CreateNewTaskService implements CreateTaskUsePort{

	private final CreateTaskDriverPort newTaskPort;
	private final AppGenericMapper<NewTaskDTOIn, NewTaskDTOOut> mapper;
	
	@Autowired
	public CreateNewTaskService(CreateTaskDriverPort newTaskPort,
			AppGenericMapper<NewTaskDTOIn, NewTaskDTOOut> mapper) {
		super();
		this.newTaskPort = newTaskPort;
		this.mapper = mapper;
	}


	@Override
	public Task create(NewTaskDTOIn task) throws AppProjectNotFoundException {
		return newTaskPort.create(mapper.map(task));
	}

}
