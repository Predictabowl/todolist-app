package it.aldinucci.todoapp.application.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.UpdateTaskUsePort;
import it.aldinucci.todoapp.application.port.in.dto.TaskDataDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadTaskByIdDriverPort;
import it.aldinucci.todoapp.application.port.out.UpdateTaskDriverPort;
import it.aldinucci.todoapp.domain.Task;

@Service
public class UpdateTaskService implements UpdateTaskUsePort{

	private LoadTaskByIdDriverPort loadTask;
	private UpdateTaskDriverPort updateTask;
	
	@Autowired
	public UpdateTaskService(LoadTaskByIdDriverPort loadTask, UpdateTaskDriverPort updateTask) {
		super();
		this.loadTask = loadTask;
		this.updateTask = updateTask;
	}


	@Override
	public Optional<Task> update(TaskIdDTO taskId, TaskDataDTOIn taskData) {
		Optional<Task> optional = loadTask.load(taskId.getTaskId());
		if (optional.isEmpty())
			return Optional.empty();
		
		Task task = optional.get();
		task.setDescription(taskData.getDescription());
		task.setName(taskData.getName());
		return updateTask.update(task);
	}

}
