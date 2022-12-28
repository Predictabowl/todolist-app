package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.Optional;

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
	
	public UpdateTaskJPA(TaskJPARepository taskRepo, AppGenericMapper<TaskJPA, Task> mapper,
			ValidateId<Long> validator) {
		super();
		this.taskRepo = taskRepo;
		this.mapper = mapper;
		this.validator = validator;
	}


	@Override
	public Optional<Task> update(Task task) {
		Optional<Long> valid = validator.getValidId(task.getId());
		if(valid.isEmpty())
			return Optional.empty();
		
		Optional<TaskJPA> optionalTaskJPA = taskRepo.findById(valid.get());
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
