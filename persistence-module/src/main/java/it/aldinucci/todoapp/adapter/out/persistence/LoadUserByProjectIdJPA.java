package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ProjectJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.util.ValidateId;
import it.aldinucci.todoapp.application.port.out.LoadUserByProjectIdDriverPort;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class LoadUserByProjectIdJPA implements LoadUserByProjectIdDriverPort {

	private AppGenericMapper<UserJPA, User> mapper;
	private ProjectJPARepository projectRepo;
	private ValidateId<Long> validator;

	@Autowired
	public LoadUserByProjectIdJPA(AppGenericMapper<UserJPA, User> mapper, ProjectJPARepository projectRepo,
			ValidateId<Long> validator) {
		super();
		this.mapper = mapper;
		this.projectRepo = projectRepo;
		this.validator = validator;
	}

	@Override
	public Optional<User> load(String projectId) throws AppProjectNotFoundException {
		if(!validator.isValid(projectId))
			return Optional.empty();

		long longId = validator.getId();
		Optional<ProjectJPA> project = projectRepo.findById(longId);
		if (project.isEmpty())
			return Optional.empty();

		return Optional.of(mapper.map(project.get().getUser()));
	}

}
