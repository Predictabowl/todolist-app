package it.aldinucci.todoapp.adapter.in.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.aldinucci.todoapp.application.port.in.ToggleTaskCompleteStatusUsePort;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@Controller
@RequestMapping("/web/project/{projectId}/task/{taskId}/toggle/completed")
public class ChangeTaskStatusWebController {
	
	private final InputModelAuthorization<TaskIdDTO> authorize;
	private final ToggleTaskCompleteStatusUsePort toggleTaskCompleted;
	
	@Autowired
	public ChangeTaskStatusWebController(InputModelAuthorization<TaskIdDTO> authorize,
			ToggleTaskCompleteStatusUsePort toggleTaskCompleted) {
		super();
		this.authorize = authorize;
		this.toggleTaskCompleted = toggleTaskCompleted;
	}


	@PostMapping
	public String toggleCompletedStatus(Authentication authentication, @PathVariable String projectId, @PathVariable TaskIdDTO taskId) {

		authorize.check(new UserIdDTO(authentication.getName()), taskId);
		toggleTaskCompleted.toggle(taskId);
		
		return "redirect:/web/project/"+projectId+"/tasks";
	}
}
