package it.aldinucci.todoapp.adapter.in.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.aldinucci.todoapp.application.port.in.ToggleTaskCompleteStatusUsePort;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@Controller
@RequestMapping("/project/{projectId}/task/{taskId}/toggle/completed")
public class ChangeTaskStatusWebController {
	
	private InputModelAuthorization<TaskIdDTO> authorize;
	private ToggleTaskCompleteStatusUsePort toggleTaskCompleted;
	
	@Autowired
	public ChangeTaskStatusWebController(InputModelAuthorization<TaskIdDTO> authorize,
			ToggleTaskCompleteStatusUsePort toggleTaskCompleted) {
		super();
		this.authorize = authorize;
		this.toggleTaskCompleted = toggleTaskCompleted;
	}


	@PostMapping
	public String toggleCompletedStatus(Authentication authentication, @PathVariable long projectId, @PathVariable long taskId) {
		TaskIdDTO idDto = new TaskIdDTO(taskId);
		authorize.check(authentication.getName(), idDto);
		toggleTaskCompleted.toggle(idDto);
		return "redirect:/project/"+projectId+"/tasks";
	}
}
