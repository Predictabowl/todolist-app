package it.aldinucci.todoapp.adapter.in.rest.controller;

import static it.aldinucci.todoapp.adapter.in.rest.config.BaseRestUrl.BASE_REST_URL;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.aldinucci.todoapp.application.port.in.LoadProjectsByUserUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.Project;

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

}
