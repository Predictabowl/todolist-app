package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.TaskJPARepository;
import it.aldinucci.todoapp.application.port.out.LoadUnfinishedTasksDriverPort;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class LoadUnfinishedTasksByProjectIdJPA implements LoadUnfinishedTasksDriverPort{

	private AppGenericMapper<TaskJPA, Task> mapper;
	private TaskJPARepository repository;
	
	@Autowired
	public LoadUnfinishedTasksByProjectIdJPA(AppGenericMapper<TaskJPA, Task> mapper, TaskJPARepository repository) {
		this.mapper = mapper;
		this.repository = repository;
	}


	@Override
	public List<Task> load(String projectId) {
		Long longId;
		try {
			longId = Long.valueOf(projectId);
		} catch (Exception e) {
			throw new AppProjectNotFoundException("Could not find project with id: "+projectId, e);
		}
		List<TaskJPA> tasks = repository.findByProjectIdAndCompletedFalse(longId);
		return tasks.stream().map(t -> mapper.map(t)).toList();
	}

}
