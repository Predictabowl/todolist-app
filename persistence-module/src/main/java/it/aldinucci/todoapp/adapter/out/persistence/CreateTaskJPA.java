package it.aldinucci.todoapp.adapter.out.persistence;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ProjectJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.repository.TaskJPARepository;
import it.aldinucci.todoapp.application.port.out.CreateTaskDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.NewTaskDTOOut;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exceptions.AppProjectNotFoundException;

@Component
public class CreateTaskJPA implements CreateTaskDriverPort{

	private ProjectJPARepository projectRepo;
	
	private TaskJPARepository taskRepo;
	
	private ModelMapper mapper;
	
	@Autowired	
	public CreateTaskJPA(ProjectJPARepository projectRepo, TaskJPARepository taskRepo, ModelMapper mapper) {
		super();
		this.projectRepo = projectRepo;
		this.taskRepo = taskRepo;
		this.mapper = mapper;
	}



	@Override
	public Task create(NewTaskDTOOut task) {
		ProjectJPA project = projectRepo.findById(task.getProjectId()).orElseThrow(() 
				-> new AppProjectNotFoundException("Project not found with id: "+task.getProjectId()));
		TaskJPA newTask = new TaskJPA(task.getName(),
				task.getDescription(),
				NewTaskDTOOut.isComplete(),
				project);
		taskRepo.save(newTask);
		project.getTasks().add(newTask);
		projectRepo.save(project);
		return mapper.map(newTask, Task.class);
	}

}
