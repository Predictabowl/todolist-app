package it.aldinucci.todoapp.adapter.in.rest.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.aldinucci.todoapp.application.port.in.DeleteTaskByIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.exception.AppTaskNotFoundException;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@RestController
@RequestMapping("/api")
public class DeleteTaskByIdRestController {

	private DeleteTaskByIdUsePort deleteTask;
	private InputModelAuthorization<TaskIdDTO> authorize;

	@Autowired
	public DeleteTaskByIdRestController(DeleteTaskByIdUsePort deleteTask,
			InputModelAuthorization<TaskIdDTO> authorize) {
		this.deleteTask = deleteTask;
		this.authorize = authorize;
	}

	@DeleteMapping("/task/{taskId}")
	public void deleteTaskEndPoint(Authentication authentication, TaskIdDTO taskId) throws AppTaskNotFoundException {
		authorize.check(authentication.getName(), taskId);
		deleteTask.delete(taskId);
	}

}
