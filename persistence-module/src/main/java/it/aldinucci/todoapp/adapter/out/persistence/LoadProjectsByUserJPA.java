package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;
import it.aldinucci.todoapp.application.port.out.LoadProjectsByUserDriverPort;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.exceptions.UserNotFoundException;

@Component
public class LoadProjectsByUserJPA implements LoadProjectsByUserDriverPort{

	private UserJPARepository userRepository;
	
	private ModelMapper mapper;
	
	@Autowired
	public LoadProjectsByUserJPA(UserJPARepository userRepository,
			ModelMapper mapper) {
		super();
		this.userRepository = userRepository;
		this.mapper = mapper;
	}

	@Override
	public List<Project> load(String userEmail) {
		UserJPA user = userRepository.findByEmail(userEmail).orElseThrow(() 
				-> new UserNotFoundException("User not found with email: "+userEmail));
		
		return user.getProjects().stream().map(p -> mapper.map(p, Project.class))
			.toList();
	}

}