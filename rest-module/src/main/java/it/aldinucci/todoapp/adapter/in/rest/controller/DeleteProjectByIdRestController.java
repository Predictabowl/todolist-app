package it.aldinucci.todoapp.adapter.in.rest.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.aldinucci.todoapp.application.port.in.DeleteProjectByIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@RestController
@RequestMapping("/api/project/{projectId}")
public class DeleteProjectByIdRestController {

	private final DeleteProjectByIdUsePort deleteProject;
	private final InputModelAuthorization<ProjectIdDTO> authorize;

	@Autowired
	public DeleteProjectByIdRestController(DeleteProjectByIdUsePort deleteProject,
			InputModelAuthorization<ProjectIdDTO> authorize) {
		this.deleteProject = deleteProject;
		this.authorize = authorize;
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteProjectEndPoint(Authentication authentication, @Valid ProjectIdDTO projectId) {
		authorize.check(new UserIdDTO(authentication.getName()), projectId);
		if (deleteProject.delete(projectId))
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
