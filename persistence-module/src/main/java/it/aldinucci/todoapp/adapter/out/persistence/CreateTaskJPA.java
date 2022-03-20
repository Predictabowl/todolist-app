package it.aldinucci.todoapp.adapter.out.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ProjectJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.repository.TaskJPARepository;
import it.aldinucci.todoapp.application.port.out.CreateTaskDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.NewTaskData;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class CreateTaskJPA implements CreateTaskDriverPort{

	private ProjectJPARepository projectRepo;
	
	private TaskJPARepository taskRepo;
	
	private AppGenericMapper<TaskJPA, Task> mapper;
	
	@Autowired
	public CreateTaskJPA(ProjectJPARepository projectRepo, TaskJPARepository taskRepo,
			AppGenericMapper<TaskJPA, Task> mapper) {
		this.projectRepo = projectRepo;
		this.taskRepo = taskRepo;
		this.mapper = mapper;
	}


	@Override
	public Task create(NewTaskData task) throws AppProjectNotFoundException{
		ProjectJPA project = projectRepo.findById(task.projectId()).orElseThrow(() 
				-> new AppProjectNotFoundException("Project not found with id: "+task.projectId()));
		TaskJPA newTask = new TaskJPA(
				null,
				task.name(),
				task.description(),
				task.complete(),
				project,
				task.orderInProject());
		taskRepo.save(newTask);
		project.getTasks().add(newTask);
		projectRepo.save(project);
		return mapper.map(newTask);
	}

}
