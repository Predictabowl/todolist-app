package it.aldinucci.todoapp.adapter.in.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.aldinucci.todoapp.application.port.in.CreateProjectUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewProjectDTOIn;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.webcommons.dto.NewProjectWebDto;

@Controller
@RequestMapping("web/project/new")
public class CreateProjectWebController {

	private CreateProjectUsePort createProject;
	
	@Autowired
	public CreateProjectWebController(CreateProjectUsePort createProject) {
		super();
		this.createProject = createProject;
	}


	@PostMapping
	public String createProject(Authentication authentication,@Valid NewProjectWebDto newProjectWebDto,
			BindingResult bindingResult) {
		if(bindingResult.hasErrors())
			return "redirect:/web";
		
		Project project = createProject.create(
				new NewProjectDTOIn(newProjectWebDto.name(),authentication.getName()));
		return "redirect:/web/project/"+project.getId()+"/tasks";
	}
}
