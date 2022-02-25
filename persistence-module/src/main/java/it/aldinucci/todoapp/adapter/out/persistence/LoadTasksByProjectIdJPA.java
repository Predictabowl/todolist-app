package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ProjectJPARepository;
import it.aldinucci.todoapp.application.port.out.LoadTasksByProjectIdDriverPort;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exceptions.AppProjectNotFoundException;

@Component
public class LoadTasksByProjectIdJPA implements LoadTasksByProjectIdDriverPort{
	
	private ProjectJPARepository projectRepository;
	
	private ModelMapper mapper;
	
	@Autowired
	public LoadTasksByProjectIdJPA(ProjectJPARepository projectRepository, ModelMapper mapper) {
		super();
		this.projectRepository = projectRepository;
		this.mapper = mapper;
	}
	

	@Override
	public List<Task> load(long projectId) {
		ProjectJPA project = projectRepository.findById(projectId).orElseThrow(()
				-> new AppProjectNotFoundException("Could not find project with id: "+projectId));
		return project.getTasks().stream().map(t -> mapper.map(t, Task.class))
				.toList();
	}


}
