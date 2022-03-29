package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ProjectJPARepository;
import it.aldinucci.todoapp.application.port.out.LoadTasksByProjectIdDriverPort;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class LoadTasksByProjectIdJPA implements LoadTasksByProjectIdDriverPort{
	
	private ProjectJPARepository projectRepository;
	
	private AppGenericMapper<TaskJPA, Task> mapper;
	
	@Autowired
	public LoadTasksByProjectIdJPA(ProjectJPARepository projectRepository, AppGenericMapper<TaskJPA, Task> mapper) {
		super();
		this.projectRepository = projectRepository;
		this.mapper = mapper;
	}


	@Override
	public List<Task> load(String projectId) throws AppProjectNotFoundException{
		ProjectJPA project = projectRepository.findById(Long.valueOf(projectId)).orElseThrow(()
				-> new AppProjectNotFoundException("Could not find project with id: "+projectId));
		return project.getTasks().stream().map(t -> mapper.map(t))
				.toList();
	}


}
