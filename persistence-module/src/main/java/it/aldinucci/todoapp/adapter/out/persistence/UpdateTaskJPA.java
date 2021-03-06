package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.TaskJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.util.ValidateId;
import it.aldinucci.todoapp.application.port.out.UpdateTaskDriverPort;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class UpdateTaskJPA implements UpdateTaskDriverPort{

	private TaskJPARepository taskRepo;
	private AppGenericMapper<TaskJPA, Task> mapper;
	private ValidateId<Long> validator;
	
	@Autowired
	public UpdateTaskJPA(TaskJPARepository taskRepo, AppGenericMapper<TaskJPA, Task> mapper,
			ValidateId<Long> validator) {
		super();
		this.taskRepo = taskRepo;
		this.mapper = mapper;
		this.validator = validator;
	}


	@Override
	public Optional<Task> update(Task task) {
		if(!validator.isValid(task.getId()))
			return Optional.empty();
		
		long longId = validator.getId();
		Optional<TaskJPA> optionalTaskJPA = taskRepo.findById(longId);
		if(optionalTaskJPA.isEmpty())
			return Optional.empty();
		
		TaskJPA taskJPA = optionalTaskJPA.get();
		taskJPA.setDescription(task.getDescription());
		taskJPA.setCompleted(task.isCompleted());
		taskJPA.setName(task.getName());
		taskJPA.setOrderInProject(task.getOrderInProject());
		return Optional.of(mapper.map(taskRepo.save(taskJPA)));
	}

}
