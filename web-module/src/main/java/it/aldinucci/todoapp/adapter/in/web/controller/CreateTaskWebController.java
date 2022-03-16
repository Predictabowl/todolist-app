package it.aldinucci.todoapp.adapter.in.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.aldinucci.todoapp.application.port.in.CreateTaskUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.webcommons.dto.NewTaskWebDto;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@Controller
@RequestMapping("/project/{projectId}/task/new")
public class CreateTaskWebController {

	private InputModelAuthorization<ProjectIdDTO> authorize;
	private CreateTaskUsePort createTask;
	
	@Autowired
	public CreateTaskWebController(InputModelAuthorization<ProjectIdDTO> authorize, CreateTaskUsePort createTask) {
		super();
		this.authorize = authorize;
		this.createTask = createTask;
	}
	
	@PostMapping
	public String createNewTask(Authentication auth, ProjectIdDTO projectId, @Valid NewTaskWebDto newTaskWebDto,
			BindingResult  bindingResult) {
		if(!bindingResult.hasErrors()) {
			authorize.check(auth.getName(), projectId);
			createTask.create(new NewTaskDTOIn(
					newTaskWebDto.name(),
					newTaskWebDto.description(),
					projectId.getProjectId()));
		}
		return "redirect:/project/"+projectId.getProjectId()+"/tasks";
	}
}
