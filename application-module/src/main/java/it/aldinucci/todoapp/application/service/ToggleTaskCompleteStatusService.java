package it.aldinucci.todoapp.application.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.ToggleTaskCompleteStatusUsePort;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadTaskByIdDriverPort;
import it.aldinucci.todoapp.application.port.out.UpdateTaskDriverPort;
import it.aldinucci.todoapp.domain.Task;

@Service
@Transactional
public class ToggleTaskCompleteStatusService implements ToggleTaskCompleteStatusUsePort{

	private LoadTaskByIdDriverPort loadTask;
	private UpdateTaskDriverPort updateTask;
	
	@Autowired
	public ToggleTaskCompleteStatusService(LoadTaskByIdDriverPort loadTask, UpdateTaskDriverPort updateTask) {
		super();
		this.loadTask = loadTask;
		this.updateTask = updateTask;
	}


	@Override
	public void toggle(TaskIdDTO taskId) {
		Optional<Task> loadedTask = loadTask.load(taskId.taskId());
		if (loadedTask.isPresent()) {
			Task task = loadedTask.get();
			task.setCompleted(!task.isCompleted());
			updateTask.update(task);
		}
	}


}
