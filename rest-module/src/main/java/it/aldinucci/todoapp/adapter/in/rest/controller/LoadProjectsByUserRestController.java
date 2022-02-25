package it.aldinucci.todoapp.adapter.in.rest.controller;

import static it.aldinucci.todoapp.webcommons.config.AppBaseUrls.BASE_REST_URL;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.aldinucci.todoapp.application.port.in.LoadProjectsByUserUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.exceptions.AppUserNotFoundException;

@RestController
@RequestMapping(BASE_REST_URL)
public class LoadProjectsByUserRestController {

	private LoadProjectsByUserUsePort loadProjects;

	@Autowired
	public LoadProjectsByUserRestController(LoadProjectsByUserUsePort loadProjects) {
		this.loadProjects = loadProjects;
	}

	@GetMapping("/projects")
	public List<Project> loadProjectsEndPoint(Authentication authentication) {
		return loadProjects.load(new UserIdDTO(authentication.getName()));
	}

	@ExceptionHandler(AppUserNotFoundException.class)
	public ResponseEntity<String> userNotFoundHandler(HttpServletRequest request, Throwable ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
