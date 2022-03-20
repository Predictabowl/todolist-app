package it.aldinucci.todoapp.adapter.in.rest.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.aldinucci.todoapp.application.port.in.CreateTaskUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@RestController
@RequestMapping("/api")
public class CreateTaskRestController {

	private CreateTaskUsePort createTask;
	private InputModelAuthorization<NewTaskDTOIn> authorize;

	@Autowired
	public CreateTaskRestController(CreateTaskUsePort createTask, InputModelAuthorization<NewTaskDTOIn> authorize) {
		this.createTask = createTask;
		this.authorize = authorize;
	}

	@PostMapping("/task/create")
	public Task createTaskEndPoint(Authentication authentication, @Valid @RequestBody NewTaskDTOIn newTask)
					throws AppProjectNotFoundException {
		authorize.check(authentication.getName(), newTask);
		return createTask.create(newTask);
	}
}
