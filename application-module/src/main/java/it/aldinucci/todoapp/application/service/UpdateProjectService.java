package it.aldinucci.todoapp.application.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.UpdateProjectUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectDataDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.out.UpdateProjectDriverPort;
import it.aldinucci.todoapp.domain.Project;

@Service
@Transactional
public class UpdateProjectService implements UpdateProjectUsePort{

	private UpdateProjectDriverPort updateProject;
	
	@Override
	public Optional<Project> update(ProjectIdDTO projectId, ProjectDataDTOIn data) {
		return updateProject.update(new Project(
				projectId.getProjectId(),
				data.getName()));
	}

}
