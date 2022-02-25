package it.aldinucci.todoapp.adapter.out.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.TaskJPARepository;
import it.aldinucci.todoapp.application.port.out.DeleteTaskByIdDriverPort;
import it.aldinucci.todoapp.exceptions.AppTaskNotFoundException;

@Component
public class DeleteTaskByIdJPA implements DeleteTaskByIdDriverPort{

	private TaskJPARepository taskRepository;
	
	@Autowired
	public DeleteTaskByIdJPA(TaskJPARepository taskRepository) {
		this.taskRepository = taskRepository;
	}


	@Override
	public void delete(long id) {
		TaskJPA task = taskRepository.findById(id).orElseThrow(()
				-> new AppTaskNotFoundException("Could not find Task with id: "+id));
		task.getProject().getTasks().remove(task);
		taskRepository.delete(task);
	}
}
