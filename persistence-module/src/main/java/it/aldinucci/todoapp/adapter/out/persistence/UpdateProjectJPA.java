package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ProjectJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.util.ValidateId;
import it.aldinucci.todoapp.application.port.out.UpdateProjectDriverPort;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class UpdateProjectJPA implements UpdateProjectDriverPort {

	private ProjectJPARepository projectRepo;
	private AppGenericMapper<ProjectJPA, Project> mapper;
	private ValidateId<Long> validator;

	@Autowired
	public UpdateProjectJPA(ProjectJPARepository projectRepo, AppGenericMapper<ProjectJPA, Project> mapper,
			ValidateId<Long> validator) {
		super();
		this.projectRepo = projectRepo;
		this.mapper = mapper;
		this.validator = validator;
	}

	@Override
	public Optional<Project> update(Project project) {
		if(!validator.isValid(project.getId()))
			return Optional.empty();

		long longId = validator.getId();
		Optional<ProjectJPA> optional = projectRepo.findById(longId);
		if (optional.isEmpty())
			return Optional.empty();

		ProjectJPA projectJPA = optional.get();
		projectJPA.setName(project.getName());
		return Optional.of(mapper.map(projectRepo.save(projectJPA)));
	}

}
