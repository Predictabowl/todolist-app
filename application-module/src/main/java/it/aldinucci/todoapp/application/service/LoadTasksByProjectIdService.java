package it.aldinucci.todoapp.application.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.LoadTasksByProjectUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadTasksByProjectIdDriverPort;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;

@Service
@Transactional
public class LoadTasksByProjectIdService implements LoadTasksByProjectUsePort{

	private final LoadTasksByProjectIdDriverPort loadTasks;
	
	public LoadTasksByProjectIdService(LoadTasksByProjectIdDriverPort loadTasks) {
		this.loadTasks = loadTasks;
	}

	@Override
	public List<Task> load(ProjectIdDTO projectId) throws AppProjectNotFoundException {
		return loadTasks.load(projectId.getProjectId()).stream().sorted(
				(t1,t2) -> t1.getOrderInProject() - t2.getOrderInProject()).toList();
	}
	
}
