package it.aldinucci.todoapp.adapter.in.rest.controller;


import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.aldinucci.todoapp.application.port.in.LoadUnfinishedTasksByProjectIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@RestController
@RequestMapping("/api/project/{projectId}/tasks/unfinished")
public class LoadUnfinishedTasksByProjectIdRestController {

	private final LoadUnfinishedTasksByProjectIdUsePort loadProjectTasks;
	private final InputModelAuthorization<ProjectIdDTO> authorize;
	
	@Autowired
	public LoadUnfinishedTasksByProjectIdRestController(LoadUnfinishedTasksByProjectIdUsePort loadProjects,
			InputModelAuthorization<ProjectIdDTO> authorize) {
		this.loadProjectTasks = loadProjects;
		this.authorize = authorize;
	}

	@GetMapping
	public List<Task> getTasksByProjectEndPoint(Authentication authentication, @Valid ProjectIdDTO projectId)
				throws AppProjectNotFoundException{
		authorize.check(new UserIdDTO(authentication.getName()), projectId);
		return loadProjectTasks.load(projectId);
	}
}
