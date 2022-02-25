package it.aldinucci.todoapp.adapter.out.persistence;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ProjectJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;
import it.aldinucci.todoapp.application.port.out.CreateProjectDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.NewProjectDTOOut;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.exceptions.AppUserNotFoundException;

@Component
public class CreateProjectJPA implements CreateProjectDriverPort{
	
	private UserJPARepository userRepository;
	
	private ProjectJPARepository projectRepository;
	
	private ModelMapper mapper;
	
	@Autowired
	public CreateProjectJPA(UserJPARepository userRepository, ProjectJPARepository projectRepository,
			ModelMapper mapper) {
		super();
		this.userRepository = userRepository;
		this.projectRepository = projectRepository;
		this.mapper = mapper;
	}

	
	@Override
	public Project create(NewProjectDTOOut project) {
		UserJPA user = userRepository.findByEmail(project.getUserEmail()).orElseThrow(() 
				->new AppUserNotFoundException("User not found with email: "+project.getUserEmail()));
		ProjectJPA projectJPA = new ProjectJPA(project.getName(), user);
		user.getProjects().add(projectJPA);
		projectJPA = projectRepository.save(projectJPA);
		userRepository.saveAndFlush(user);
		return mapper.map(projectJPA, Project.class);
	}


}
