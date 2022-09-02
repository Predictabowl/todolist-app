package it.aldinucci.todoapp.adapter.in.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.aldinucci.todoapp.application.port.in.ToggleTaskCompleteStatusUsePort;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.exception.AppTaskNotFoundException;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@RestController
@RequestMapping("/api/task/{taskId}/completed/toggle")
public class ToggleTaskCompleteStatusRestController {

	private final InputModelAuthorization<TaskIdDTO> authorize;
	private final ToggleTaskCompleteStatusUsePort toggleTaskStatus;
	
	@Autowired
	public ToggleTaskCompleteStatusRestController(InputModelAuthorization<TaskIdDTO> authorize,
			ToggleTaskCompleteStatusUsePort toggleTaskStatus) {
		super();
		this.authorize = authorize;
		this.toggleTaskStatus = toggleTaskStatus;
	}

	@PutMapping
	public void toggleTaskCompleted(Authentication authentication, TaskIdDTO taskId)
				throws AppTaskNotFoundException{
		authorize.check(new UserIdDTO(authentication.getName()), taskId);
		toggleTaskStatus.toggle(taskId);
	}
	
}