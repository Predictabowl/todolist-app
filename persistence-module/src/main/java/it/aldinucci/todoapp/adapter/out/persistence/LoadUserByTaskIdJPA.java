package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.TaskJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.util.ValidateId;
import it.aldinucci.todoapp.application.port.out.LoadUserByTaskIdDriverPort;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exception.AppTaskNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class LoadUserByTaskIdJPA implements LoadUserByTaskIdDriverPort {

	private AppGenericMapper<UserJPA, User> mapper;
	private TaskJPARepository repository;
	private ValidateId<Long> validator;

	public LoadUserByTaskIdJPA(AppGenericMapper<UserJPA, User> mapper, TaskJPARepository repository,
			ValidateId<Long> validator) {
		super();
		this.mapper = mapper;
		this.repository = repository;
		this.validator = validator;
	}

	@Override
	public Optional<User> load(String taskId) throws AppTaskNotFoundException {
		Optional<Long> valid = validator.getValidId(taskId);
		if(valid.isEmpty())
			return Optional.empty();

		Optional<TaskJPA> task = repository.findById(valid.get());
		if (task.isEmpty())
			return Optional.empty();

		return Optional.of(mapper.map(task.get().getProject().getUser()));
	}

}
