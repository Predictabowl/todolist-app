package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.TaskJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.util.ValidateId;
import it.aldinucci.todoapp.application.port.out.DeleteTaskByIdDriverPort;

@Component
public class DeleteTaskByIdJPA implements DeleteTaskByIdDriverPort{

	private TaskJPARepository taskRepository;
	private ValidateId<Long> validator;
	
	@Autowired
	public DeleteTaskByIdJPA(TaskJPARepository taskRepository, ValidateId<Long> validator) {
		super();
		this.taskRepository = taskRepository;
		this.validator = validator;
	}

	@Override
	public boolean delete(String id){
		if (!validator.isValid(id))
			return false;

		long longId = validator.getId();
		Optional<TaskJPA> optional = taskRepository.findById(longId);
		if (optional.isEmpty())
			return false;
		
		TaskJPA taskJPA = optional.get();
		taskJPA.getProject().getTasks().remove(taskJPA);
		taskRepository.delete(taskJPA);
		return true;
	}
}
