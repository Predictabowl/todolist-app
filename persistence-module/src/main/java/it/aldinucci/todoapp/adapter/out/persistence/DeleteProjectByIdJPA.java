package it.aldinucci.todoapp.adapter.out.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ProjectJPARepository;
import it.aldinucci.todoapp.application.port.out.DeleteProjectByIdDriverPort;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;

@Component
public class DeleteProjectByIdJPA implements DeleteProjectByIdDriverPort{

	private ProjectJPARepository projectRepository;
	
	@Autowired
	public DeleteProjectByIdJPA(ProjectJPARepository projectRepository) {
		this.projectRepository = projectRepository;
	}

	@Override
	public void delete(String id) throws AppProjectNotFoundException{
		long longId;
		try {
			longId = Long.parseLong(id);
		} catch (NumberFormatException e) {
			throw new AppProjectNotFoundException("Could not find Project", e);
		}
		
		
		ProjectJPA project = projectRepository.findById(longId).orElseThrow(() 
				-> new AppProjectNotFoundException("Could not find Project with id: "+id));
		project.getUser().getProjects().remove(project);
		projectRepository.delete(project);
	}

}
