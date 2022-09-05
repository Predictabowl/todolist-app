package it.aldinucci.todoapp.adapter.in.rest.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.aldinucci.todoapp.application.port.in.UpdateTaskUsePort;
import it.aldinucci.todoapp.application.port.in.dto.TaskDataDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exception.AppTaskNotFoundException;
import it.aldinucci.todoapp.webcommons.dto.TaskDataWebDto;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@RestController
@RequestMapping("/api/task/{taskId}")
public class UpdateTaskRestController {
	
	private final InputModelAuthorization<TaskIdDTO> authorize;
	private final UpdateTaskUsePort updateTask;
	
	@Autowired
	public UpdateTaskRestController(InputModelAuthorization<TaskIdDTO> authorize, UpdateTaskUsePort updateTask) {
		super();
		this.authorize = authorize;
		this.updateTask = updateTask;
	}


	@PutMapping
	public ResponseEntity<Optional<Task>> updateTaskEndPoint(Authentication authentication,
				@PathVariable TaskIdDTO taskId, @Valid @RequestBody TaskDataWebDto taskData) 
						throws AppTaskNotFoundException{
		
		authorize.check(new UserIdDTO(authentication.getName()), taskId);
		Optional<Task> optional = updateTask.update(taskId, new TaskDataDTOIn(
				taskData.name(), taskData.description()));
		if (optional.isEmpty())
			return new ResponseEntity<>(optional, HttpStatus.BAD_REQUEST);
		
		return new ResponseEntity<>(optional, HttpStatus.OK);
	}

}
