package it.aldinucci.todoapp.adapter.in.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import it.aldinucci.todoapp.application.port.in.DeleteTaskByIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@Controller
@RequestMapping("/web/project/{projectId}/task/{taskId}")
public class DeleteTaskWebController {

	private InputModelAuthorization<TaskIdDTO> authorize;
	private DeleteTaskByIdUsePort deleteTask;
	
	@Autowired
	public DeleteTaskWebController(InputModelAuthorization<TaskIdDTO> authorize, DeleteTaskByIdUsePort deleteTask) {
		super();
		this.authorize = authorize;
		this.deleteTask = deleteTask;
	}

	@DeleteMapping
	public String deleteTaskEndPoint(Authentication authentication, @PathVariable long projectId, @PathVariable long taskId) {
		TaskIdDTO idDTO = new TaskIdDTO(taskId);
		authorize.check(authentication.getName(), idDTO);
		deleteTask.delete(idDTO);
		return "redirect:/web/project/"+projectId+"/tasks";
	}
}
