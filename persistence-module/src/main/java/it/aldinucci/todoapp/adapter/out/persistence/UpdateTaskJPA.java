package it.aldinucci.todoapp.adapter.out.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.TaskJPARepository;
import it.aldinucci.todoapp.application.port.out.UpdateTaskDriverPort;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exception.AppTaskNotFoundException;

@Component
public class UpdateTaskJPA implements UpdateTaskDriverPort{

	private TaskJPARepository taskRepo;
	
	@Autowired
	public UpdateTaskJPA(TaskJPARepository taskRepo) {
		super();
		this.taskRepo = taskRepo;
	}

	@Override
	public void update(Task task) {
		TaskJPA taskJPA = taskRepo.findById(task.getId()).
				orElseThrow(() -> new AppTaskNotFoundException("Could not find task with id: "+task.getId()));
		
		taskJPA.setDescription(task.getDescription());
		taskJPA.setCompleted(task.isCompleted());
		taskJPA.setName(task.getName());
		taskRepo.save(taskJPA);
	}

}
