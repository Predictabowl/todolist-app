package it.aldinucci.todoapp.adapter.out.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ProjectJPARepository;
import it.aldinucci.todoapp.application.port.out.LoadUserByProjectIdDriverPort;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exceptions.AppProjectNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class LoadUserByProjectIdJPA implements LoadUserByProjectIdDriverPort{

	private AppGenericMapper<UserJPA, User> mapper;
	
	private ProjectJPARepository projectRepo;
	
	@Autowired
	public LoadUserByProjectIdJPA(AppGenericMapper<UserJPA, User> mapper, ProjectJPARepository projectRepo) {
		this.mapper = mapper;
		this.projectRepo = projectRepo;
	}


	@Override
	public User load(long projecId) {
		ProjectJPA project = projectRepo.findById(projecId)
				.orElseThrow(() -> new AppProjectNotFoundException("Project not found with id: "+projecId));
		return mapper.map(project.getUser());
	}

}
