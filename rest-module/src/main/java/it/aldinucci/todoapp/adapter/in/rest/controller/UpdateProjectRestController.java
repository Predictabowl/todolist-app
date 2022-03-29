package it.aldinucci.todoapp.adapter.in.rest.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.aldinucci.todoapp.application.port.in.UpdateProjectUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectDataDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;
import it.aldinucci.todoapp.webcommons.dto.ProjectDataWebDto;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@RestController
@RequestMapping("/api/project/{projectId}")
public class UpdateProjectRestController {
	
	private InputModelAuthorization<ProjectIdDTO> authorize;
	private UpdateProjectUsePort updateProject;
	private AppGenericMapper<ProjectDataWebDto, ProjectDataDTOIn> mapper;
	
	@Autowired
	public UpdateProjectRestController(InputModelAuthorization<ProjectIdDTO> authorize,
			UpdateProjectUsePort updateProject, AppGenericMapper<ProjectDataWebDto, ProjectDataDTOIn> mapper) {
		super();
		this.authorize = authorize;
		this.updateProject = updateProject;
		this.mapper = mapper;
	}


	@PutMapping
	public ResponseEntity<Optional<Project>> updateProjectEndPoint(Authentication authentication,
				@PathVariable String projectId, @Valid @RequestBody ProjectDataWebDto projectData) 
						throws AppProjectNotFoundException{
		
		ProjectIdDTO idDTO = new ProjectIdDTO(projectId);
		authorize.check(authentication.getName(), idDTO);
		Optional<Project> optional = updateProject.update(idDTO, mapper.map(projectData));
		if (optional.isEmpty())
			return new ResponseEntity<>(optional, HttpStatus.BAD_REQUEST);
		
		return new ResponseEntity<>(optional, HttpStatus.OK);
	}

}
