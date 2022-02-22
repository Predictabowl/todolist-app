package it.aldinucci.todoapp.adapter.in.rest.controller;

import static it.aldinucci.todoapp.adapter.in.rest.config.BaseRestUrl.BASE_REST_URL;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.aldinucci.todoapp.application.port.in.CreateProjectUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewProjectDTOIn;
import it.aldinucci.todoapp.domain.Project;

@RestController
@RequestMapping(BASE_REST_URL)
public class CreateProjectRestController {

	private CreateProjectUsePort createProject;
	
	@Autowired
	public CreateProjectRestController(CreateProjectUsePort createProject) {
		this.createProject = createProject;
	}

	@PostMapping("/create")
	public Project createProjectEndPoint(@Valid @RequestBody NewProjectDTOIn newProject) {
		return createProject.create(newProject);
	}
}
