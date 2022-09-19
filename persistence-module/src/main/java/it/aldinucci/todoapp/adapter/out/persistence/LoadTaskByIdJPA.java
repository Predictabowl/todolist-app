package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.TaskJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.util.ValidateId;
import it.aldinucci.todoapp.application.port.out.LoadTaskByIdDriverPort;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class LoadTaskByIdJPA implements LoadTaskByIdDriverPort {

	private TaskJPARepository taskRepo;
	private AppGenericMapper<TaskJPA, Task> mapper;
	private ValidateId<Long> validator;

	@Autowired
	public LoadTaskByIdJPA(TaskJPARepository taskRepo, AppGenericMapper<TaskJPA, Task> mapper,
			ValidateId<Long> validator) {
		super();
		this.taskRepo = taskRepo;
		this.mapper = mapper;
		this.validator = validator;
	}

	@Override
	public Optional<Task> load(String id) {
		Optional<Long> valid = validator.isValid(id);
		if (valid.isEmpty())
			return Optional.empty();

		Optional<TaskJPA> loadedTask = taskRepo.findById(valid.get());
		if (loadedTask.isEmpty())
			return Optional.empty();
		return Optional.of(mapper.map(loadedTask.get()));
	}

}
