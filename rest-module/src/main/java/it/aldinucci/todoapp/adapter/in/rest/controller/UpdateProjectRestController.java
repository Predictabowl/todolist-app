package it.aldinucci.todoapp.adapter.in.rest.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.aldinucci.todoapp.application.port.in.UpdateProjectUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectDataDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.webcommons.dto.ProjectDataWebDto;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@RestController
@RequestMapping("/api/project")
public class UpdateProjectRestController {
	
	private InputModelAuthorization<ProjectIdDTO> authorize;
	private UpdateProjectUsePort updateProject;
	
	@Autowired
	public UpdateProjectRestController(InputModelAuthorization<ProjectIdDTO> authorize,
			UpdateProjectUsePort updateProject) {
		super();
		this.authorize = authorize;
		this.updateProject = updateProject;
	}

	@PutMapping
	public ResponseEntity<Optional<Project>> updateProjectEndPoint(Authentication authentication,
				@Valid @RequestBody ProjectDataWebDto projectData) {
		
		ProjectIdDTO idDTO = new ProjectIdDTO(projectData.id());
		authorize.check(authentication.getName(), idDTO);
		Optional<Project> optional = updateProject.update(idDTO, new ProjectDataDTOIn(projectData.name()));
		if (optional.isEmpty())
			return new ResponseEntity<>(optional, HttpStatus.BAD_REQUEST);
		
		return new ResponseEntity<>(optional, HttpStatus.OK);
	}

}
