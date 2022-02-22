package it.aldinucci.todoapp.adapter.out.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ProjectJPARepository;
import it.aldinucci.todoapp.application.port.out.DeleteProjectByIdDriverPort;
import it.aldinucci.todoapp.exceptions.ProjectNotFoundException;

@Component
public class DeleteProjectByIdJPA implements DeleteProjectByIdDriverPort{

	private ProjectJPARepository projectRepository;
	
	@Autowired
	public DeleteProjectByIdJPA(ProjectJPARepository projectRepository) {
		this.projectRepository = projectRepository;
	}

	@Override
	public void delete(long id) {
		ProjectJPA project = projectRepository.findById(id).orElseThrow(() 
				-> new ProjectNotFoundException("Could not find Project with id: "+id));
		project.getUser().getProjects().remove(project);
		projectRepository.delete(project);
	}

}
