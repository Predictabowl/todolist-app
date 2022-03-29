package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.TaskJPARepository;
import it.aldinucci.todoapp.application.port.out.LoadTaskByIdDriverPort;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class LoadTaskByIdJPA implements LoadTaskByIdDriverPort{

	private TaskJPARepository taskRepo;
	private AppGenericMapper<TaskJPA, Task> mapper;
	
	@Autowired
	public LoadTaskByIdJPA(TaskJPARepository taskRepo, AppGenericMapper<TaskJPA, Task> mapper) {
		super();
		this.taskRepo = taskRepo;
		this.mapper = mapper;
	}

	@Override
	public Optional<Task> load(String id) {
		long longId;
		try {
			longId = Long.parseLong(id);
		}catch (NumberFormatException e) {
			return Optional.empty();
		}
		
		Optional<TaskJPA> loadedTask = taskRepo.findById(longId);
		if(loadedTask.isEmpty())
			return Optional.empty();
		return Optional.of(mapper.map(loadedTask.get()));
	}

}
