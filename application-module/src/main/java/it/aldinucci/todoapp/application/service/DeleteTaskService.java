package it.aldinucci.todoapp.application.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.DeleteTaskByIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.application.port.out.DeleteTaskByIdDriverPort;
import it.aldinucci.todoapp.exceptions.AppTaskNotFoundException;

@Service
@Transactional
public class DeleteTaskService implements DeleteTaskByIdUsePort{
	
	private final DeleteTaskByIdDriverPort deletePort;

	@Autowired
	public DeleteTaskService(DeleteTaskByIdDriverPort deletePort) {
		this.deletePort = deletePort;
	}

	@Override
	public void delete(TaskIdDTO task) throws AppTaskNotFoundException {
		deletePort.delete(task.getTaskId());
	}

}
