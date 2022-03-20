package it.aldinucci.todoapp.adapter.in.rest.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.aldinucci.todoapp.application.port.in.LoadProjectsByUserUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;

@RestController
@RequestMapping("/api/projects")
public class LoadProjectsByUserRestController {

	private LoadProjectsByUserUsePort loadProjects;

	@Autowired
	public LoadProjectsByUserRestController(LoadProjectsByUserUsePort loadProjects) {
		this.loadProjects = loadProjects;
	}

	@GetMapping
	public List<Project> loadProjectsEndPoint(Authentication authentication) throws AppUserNotFoundException {
		return loadProjects.load(new UserIdDTO(authentication.getName()));
	}

}
