package it.aldinucci.todoapp.webadapter.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.aldinucci.todoapp.application.port.in.CreateProjectUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewProjectDTOIn;
import it.aldinucci.todoapp.domain.Project;

@RestController
@RequestMapping("/api/test")
public class TempRestController {
	
	@Autowired
	private CreateProjectUsePort port;
	
	@PostMapping("/new")
	public Project newProject(@Valid @RequestBody NewProjectDTOIn newProject) {
		return port.create(newProject);
	}

}
