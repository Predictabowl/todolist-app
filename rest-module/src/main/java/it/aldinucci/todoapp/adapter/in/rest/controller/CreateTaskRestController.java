package it.aldinucci.todoapp.adapter.in.rest.controller;


import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.aldinucci.todoapp.application.port.in.CreateTaskUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;
import it.aldinucci.todoapp.webcommons.dto.TaskDataWebDto;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@RestController
@RequestMapping("/api/project/{projectId}/task")
public class CreateTaskRestController {

	private final CreateTaskUsePort createTask;
	private final InputModelAuthorization<NewTaskDTOIn> authorize;

	public CreateTaskRestController(CreateTaskUsePort createTask, InputModelAuthorization<NewTaskDTOIn> authorize) {
		this.createTask = createTask;
		this.authorize = authorize;
	}

	@PostMapping
	public Task createTaskEndPoint(Authentication authentication, @PathVariable String projectId, 
				@Valid @RequestBody TaskDataWebDto taskData)	throws AppProjectNotFoundException {
		
		NewTaskDTOIn newTask = new NewTaskDTOIn(taskData.name(), taskData.description(), projectId);
		authorize.check(new UserIdDTO(authentication.getName()), newTask);
		return createTask.create(newTask);
	}
}
