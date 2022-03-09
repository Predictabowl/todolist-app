package it.aldinucci.todoapp.adapter.in.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.aldinucci.todoapp.adapter.in.web.dto.NewProjectWebDto;
import it.aldinucci.todoapp.application.port.in.CreateProjectUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewProjectDTOIn;

@Controller
@RequestMapping("/project/new")
public class CreateProjectWebController {

	private CreateProjectUsePort createProject;
	
	@Autowired
	public CreateProjectWebController(CreateProjectUsePort createProject) {
		super();
		this.createProject = createProject;
	}


	@PostMapping
	public String createProject(NewProjectWebDto newProjectWebDto, Authentication authentication) {
		createProject.create(new NewProjectDTOIn(newProjectWebDto.getName(), authentication.getName()));
		return "redirect:/";
	}
}
