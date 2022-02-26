package it.aldinucci.todoapp.adapter.in.rest.controller;

import static it.aldinucci.todoapp.webcommons.config.AppBaseURIs.BASE_REST_URI;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.aldinucci.todoapp.application.port.in.DeleteTaskByIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.exceptions.AppTaskNotFoundException;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@RestController
@RequestMapping(BASE_REST_URI)
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
	public void deleteTaskEndPoint(Authentication authentication, TaskIdDTO taskId) {
		authorize.check(authentication.getName(), taskId);
		deleteTask.delete(taskId);
	}
	
	@ExceptionHandler(AppTaskNotFoundException.class)
	public ResponseEntity<String> notFoundHandler(HttpServletRequest request, Throwable ex) {
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
