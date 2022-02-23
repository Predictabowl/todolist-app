package it.aldinucci.todoapp.adapter.in.rest.controller;

import static it.aldinucci.todoapp.webcommons.config.AppBaseUrls.BASE_REST_URL;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.aldinucci.todoapp.application.port.in.CreateTaskUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exceptions.AppProjectNotFoundException;

@RestController
@RequestMapping(BASE_REST_URL)
public class CreateTaskRestController {

	private CreateTaskUsePort createTask;
	
	@Autowired
	public CreateTaskRestController(CreateTaskUsePort createTask) {
		this.createTask = createTask;
	}

	@PostMapping("/task/create")
	public Task createTaskEndPoint(@Valid @RequestBody NewTaskDTOIn newTask) {
		return createTask.create(newTask);
	}
	
	@ExceptionHandler(AppProjectNotFoundException.class)
	public ResponseEntity<String> userNotFoundHandler(HttpServletRequest request, Throwable ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
