package it.aldinucci.todoapp.adapter.out.persistence;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ProjectJPARepository;
import it.aldinucci.todoapp.application.port.out.LoadUserByProjectIdDriverPort;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exceptions.AppProjectNotFoundException;

@Component
public class LoadUserByProjectIdJPA implements LoadUserByProjectIdDriverPort{

	private ModelMapper mapper;
	
	private ProjectJPARepository projectRepo;
	
	@Autowired
	public LoadUserByProjectIdJPA(ModelMapper mapper, ProjectJPARepository projectRepo) {
		this.mapper = mapper;
		this.projectRepo = projectRepo;
	}

	@Override
	public User load(long projecId) {
		ProjectJPA project = projectRepo.findById(projecId)
				.orElseThrow(() -> new AppProjectNotFoundException("Project not found with id: "+projecId));
		return mapper.map(project.getUser(),User.class);
	}

}
