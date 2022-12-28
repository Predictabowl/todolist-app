package it.aldinucci.todoapp.adapter.in.web.controller;

import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.aldinucci.todoapp.application.port.in.CreateTaskUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.webcommons.dto.TaskDataWebDto;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@Controller
@RequestMapping("/web/project/{projectId}/task/new")
public class CreateTaskWebController {

	private final InputModelAuthorization<ProjectIdDTO> authorize;
	private final CreateTaskUsePort createTask;
	
	public CreateTaskWebController(InputModelAuthorization<ProjectIdDTO> authorize, CreateTaskUsePort createTask) {
		super();
		this.authorize = authorize;
		this.createTask = createTask;
	}
	
	@PostMapping
	public String createNewTask(Authentication auth,@PathVariable ProjectIdDTO projectId, @Valid TaskDataWebDto newTaskWebDto,
			BindingResult  bindingResult) {
		
		authorize.check(new UserIdDTO(auth.getName()), projectId);
		if(!bindingResult.hasErrors()) {
			createTask.create(new NewTaskDTOIn(
					newTaskWebDto.name(),
					newTaskWebDto.description(),
					projectId.getProjectId()));
		}
		return "redirect:/web/project/"+projectId.getProjectId()+"/tasks";
	}
}
