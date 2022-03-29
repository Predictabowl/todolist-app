package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.OptionalInt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ProjectJPARepository;
import it.aldinucci.todoapp.application.port.out.GetTaskMaxOrderInProjectDriverPort;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;

@Component
public class GetTaskMaxOrderInProjectJPA implements GetTaskMaxOrderInProjectDriverPort{

	private ProjectJPARepository projectRepo;
	
	@Autowired
	public GetTaskMaxOrderInProjectJPA(ProjectJPARepository projectRepo) {
		super();
		this.projectRepo = projectRepo;
	}

	@Override
	public OptionalInt get(String projectId) throws AppProjectNotFoundException {
		ProjectJPA project = projectRepo.findById(Long.valueOf(projectId)).orElseThrow(() -> 
			new AppProjectNotFoundException("Could not find Project with id: "+projectId));
		
		return project.getTasks().stream().mapToInt(TaskJPA::getOrderInProject).max(); 
	}

}
