package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ProjectJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.util.ValidateId;
import it.aldinucci.todoapp.application.port.out.DeleteProjectByIdDriverPort;

@Component
public class DeleteProjectByIdJPA implements DeleteProjectByIdDriverPort{

	private ProjectJPARepository projectRepository;
	private ValidateId<Long> validator;
	
	public DeleteProjectByIdJPA(ProjectJPARepository projectRepository, ValidateId<Long> validator) {
		super();
		this.projectRepository = projectRepository;
		this.validator = validator;
	}

	@Override
	public boolean delete(String id){
		Optional<Long> valid = validator.getValidId(id);
		if(valid.isEmpty())
			return false;

		Optional<ProjectJPA> optionalProject = projectRepository.findById(valid.get());
		if(optionalProject.isEmpty())
			return false;
		
		ProjectJPA project = optionalProject.get();
		project.getUser().getProjects().remove(project);
		projectRepository.delete(project);
		return true;
	}

}
