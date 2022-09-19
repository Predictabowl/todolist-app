package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.TaskJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.util.ValidateId;
import it.aldinucci.todoapp.application.port.out.LoadUnfinishedTasksDriverPort;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class LoadUnfinishedTasksByProjectIdJPA implements LoadUnfinishedTasksDriverPort {

	private AppGenericMapper<TaskJPA, Task> mapper;
	private TaskJPARepository repository;
	private ValidateId<Long> validator;

	@Autowired
	public LoadUnfinishedTasksByProjectIdJPA(AppGenericMapper<TaskJPA, Task> mapper, TaskJPARepository repository,
			ValidateId<Long> validator) {
		super();
		this.mapper = mapper;
		this.repository = repository;
		this.validator = validator;
	}

	@Override
	public List<Task> load(String projectId) {
		long longId = validator.isValid(projectId).orElseThrow(() ->
			new AppProjectNotFoundException("Could not find project with id: " + projectId));

		List<TaskJPA> tasks = repository.findByProjectIdAndCompletedFalse(longId);
		return tasks.stream().map(t -> mapper.map(t)).toList();
	}

}
