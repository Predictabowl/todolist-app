package it.aldinucci.todoapp.adapter.in.rest.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.aldinucci.todoapp.application.port.in.LoadTasksByProjectUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exceptions.AppProjectNotFoundException;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@RestController
@RequestMapping("/api")
public class LoadTaskByProjectIdRestController {

	private LoadTasksByProjectUsePort loadProjectTasks;
	private InputModelAuthorization<ProjectIdDTO> authorize;
	
	@Autowired
	public LoadTaskByProjectIdRestController(LoadTasksByProjectUsePort loadProjects,
			InputModelAuthorization<ProjectIdDTO> authorize) {
		this.loadProjectTasks = loadProjects;
		this.authorize = authorize;
	}

	@GetMapping("/project/{projectId}/tasks")
	public List<Task> getTasksByProjectEndPoint(Authentication authentication, @Valid ProjectIdDTO projectId) throws AppProjectNotFoundException{
		authorize.check(authentication.getName(), projectId);
		return loadProjectTasks.load(projectId);
	}
	
	@ExceptionHandler(AppProjectNotFoundException.class)
	public ResponseEntity<String> userNotFoundHandler(HttpServletRequest request, Throwable ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
		
}
