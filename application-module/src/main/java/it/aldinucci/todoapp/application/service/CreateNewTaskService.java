package it.aldinucci.todoapp.application.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.NewTaskUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.application.port.out.LoadProjectByIdDriverPort;
import it.aldinucci.todoapp.application.port.out.NewTaskDriverPort;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.domain.Task;

@Service
@Transactional
class CreateNewTaskService implements NewTaskUsePort{

	private final NewTaskDriverPort newTaskPort;
	private final LoadProjectByIdDriverPort loadProjectPort;
	
	@Autowired
	public CreateNewTaskService(NewTaskDriverPort newTaskPort, LoadProjectByIdDriverPort loadProjectPort) {
		this.newTaskPort = newTaskPort;
		this.loadProjectPort = loadProjectPort;
	}

	@Override
	public Task create(NewTaskDTOIn task) {
		Project project = loadProjectPort.loadById(task.getProjectId());
		Task newTask = new Task(null, task.getName(), task.getDescription());
		newTask.setProject(project);
		return newTaskPort.save(newTask);
	}
}
