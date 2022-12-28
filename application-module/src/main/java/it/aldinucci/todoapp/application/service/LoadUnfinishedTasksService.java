package it.aldinucci.todoapp.application.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.LoadUnfinishedTasksByProjectIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadUnfinishedTasksDriverPort;
import it.aldinucci.todoapp.domain.Task;

@Service
@Transactional
public class LoadUnfinishedTasksService implements LoadUnfinishedTasksByProjectIdUsePort{

	private final LoadUnfinishedTasksDriverPort loadTasks;
	
	public LoadUnfinishedTasksService(LoadUnfinishedTasksDriverPort loadTasks) {
		this.loadTasks = loadTasks;
	}

	@Override
	public List<Task> load(ProjectIdDTO projectId) {
		return loadTasks.load(projectId.getProjectId());
	}

}
