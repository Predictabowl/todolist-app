package it.aldinucci.todoapp.application.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.DeleteTaskByIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.application.port.out.DeleteTaskByIdDriverPort;

@Service
@Transactional
public class DeleteTaskService implements DeleteTaskByIdUsePort{
	
	private final DeleteTaskByIdDriverPort deletePort;

	public DeleteTaskService(DeleteTaskByIdDriverPort deletePort) {
		this.deletePort = deletePort;
	}

	@Override
	public boolean delete(TaskIdDTO task){
		return deletePort.delete(task.getTaskId());
	}

}
