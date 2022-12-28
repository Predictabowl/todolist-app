package it.aldinucci.todoapp.adapter.in.rest.controller;


import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.aldinucci.todoapp.application.port.in.CreateProjectUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewProjectDTOIn;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;
import it.aldinucci.todoapp.webcommons.dto.ProjectDataWebDto;

@RestController
@RequestMapping("/api/project")
public class CreateProjectRestController {

	private final CreateProjectUsePort createProject;

	public CreateProjectRestController(CreateProjectUsePort createProject) {
		this.createProject = createProject;
	}

	@PostMapping
	public Project createProjectEndPoint(Authentication authentication,
			@Valid @RequestBody ProjectDataWebDto newProject) throws AppUserNotFoundException {
		
		NewProjectDTOIn projectDto = new NewProjectDTOIn(newProject.name(), authentication.getName());
		return createProject.create(projectDto);
	}
}
