package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.TaskJPARepository;
import it.aldinucci.todoapp.application.port.out.DeleteTaskByIdDriverPort;

@Component
public class DeleteTaskByIdJPA implements DeleteTaskByIdDriverPort{

	private TaskJPARepository taskRepository;
	
	@Autowired
	public DeleteTaskByIdJPA(TaskJPARepository taskRepository) {
		this.taskRepository = taskRepository;
	}


	@Override
	public void delete(long id){
		Optional<TaskJPA> optional = taskRepository.findById(id);
		if (optional.isPresent()) {		
			TaskJPA taskJPA = optional.get();
			taskJPA.getProject().getTasks().remove(taskJPA);
			taskRepository.delete(taskJPA);
		}
	}
}
