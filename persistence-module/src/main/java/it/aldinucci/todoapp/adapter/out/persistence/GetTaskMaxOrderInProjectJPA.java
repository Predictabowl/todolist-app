package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.OptionalInt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ProjectJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.util.ValidateId;
import it.aldinucci.todoapp.application.port.out.GetTaskMaxOrderInProjectDriverPort;
import it.aldinucci.todoapp.exception.AppInvalidIdException;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;

@Component
public class GetTaskMaxOrderInProjectJPA implements GetTaskMaxOrderInProjectDriverPort{

	private ProjectJPARepository projectRepo;
	private ValidateId<Long> validator;
	
	@Autowired
	public GetTaskMaxOrderInProjectJPA(ProjectJPARepository projectRepo, ValidateId<Long> validator) {
		super();
		this.projectRepo = projectRepo;
		this.validator = validator;
	}


	@Override
	public OptionalInt get(String projectId) throws AppProjectNotFoundException, AppInvalidIdException {
		if (!validator.isValid(projectId))
			throw new AppInvalidIdException("Invalid Project id: " + projectId);

		long longId = validator.getId();
		ProjectJPA project = projectRepo.findById(longId).orElseThrow(() -> 
			new AppProjectNotFoundException("Could not find Project with id: "+projectId));
		
		return project.getTasks().stream().mapToInt(TaskJPA::getOrderInProject).max(); 
	}

}
