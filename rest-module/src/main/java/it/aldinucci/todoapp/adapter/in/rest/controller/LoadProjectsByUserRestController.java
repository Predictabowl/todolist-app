package it.aldinucci.todoapp.adapter.in.rest.controller;

import static it.aldinucci.todoapp.adapter.in.rest.config.BaseRestUrl.BASE_REST_URL;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.aldinucci.todoapp.application.port.in.LoadProjectsByUserUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.exceptions.UserNotFoundException;

@RestController
@RequestMapping(BASE_REST_URL)
public class LoadProjectsByUserRestController {
	
	private LoadProjectsByUserUsePort loadProjects;

	@Autowired
	public LoadProjectsByUserRestController(LoadProjectsByUserUsePort loadProjects) {
		super();
		this.loadProjects = loadProjects;
	}
	
	@GetMapping("/{email}/projects")
	public List<Project> loadProjectsEndPoint(@Valid UserIdDTO userId){
		return loadProjects.load(userId);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> userNotFoundHandler(HttpServletRequest request, Throwable ex){
		return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
	}
}