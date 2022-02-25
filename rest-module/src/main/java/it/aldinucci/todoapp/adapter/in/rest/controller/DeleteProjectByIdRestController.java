package it.aldinucci.todoapp.adapter.in.rest.controller;

import static it.aldinucci.todoapp.webcommons.config.AppBaseURIs.BASE_REST_URI;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.aldinucci.todoapp.application.port.in.DeleteProjectByIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.exceptions.AppProjectNotFoundException;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@RestController
@RequestMapping(BASE_REST_URI)
public class DeleteProjectByIdRestController{

	private DeleteProjectByIdUsePort deleteProject;
	private InputModelAuthorization<ProjectIdDTO> authorize;
	
	@Autowired
	public DeleteProjectByIdRestController(DeleteProjectByIdUsePort deleteProject,
			InputModelAuthorization<ProjectIdDTO> authorize) {
		this.deleteProject = deleteProject;
		this.authorize = authorize;
	}


	@DeleteMapping("/project/{projectId}")
	public void deleteProjectEndPoint(Authentication authentication,@Valid ProjectIdDTO projectId) {
		authorize.check(authentication.getName(), projectId);
		deleteProject.delete(projectId);
	}
	
	@ExceptionHandler(AppProjectNotFoundException.class)
	public ResponseEntity<String> notFoundHandler(HttpServletRequest request, Throwable ex) {
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
