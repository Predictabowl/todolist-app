package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ProjectJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.util.ValidateId;
import it.aldinucci.todoapp.application.port.out.DeleteProjectByIdDriverPort;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;

@Component
public class DeleteProjectByIdJPA implements DeleteProjectByIdDriverPort{

	private ProjectJPARepository projectRepository;
	private ValidateId<Long> validator;
	
	@Autowired
	public DeleteProjectByIdJPA(ProjectJPARepository projectRepository, ValidateId<Long> validator) {
		super();
		this.projectRepository = projectRepository;
		this.validator = validator;
	}


	@Override
	public boolean delete(String id) throws AppProjectNotFoundException{
		Optional<Long> valid = validator.getValidId(id);
		if(valid.isEmpty())
			return false;

		long longId = valid.get();
		Optional<ProjectJPA> optionalProject = projectRepository.findById(longId);
		if(optionalProject.isEmpty())
			return false;
		
		ProjectJPA project = optionalProject.get();
		project.getUser().getProjects().remove(project);
		projectRepository.delete(project);
		return true;
	}

}
