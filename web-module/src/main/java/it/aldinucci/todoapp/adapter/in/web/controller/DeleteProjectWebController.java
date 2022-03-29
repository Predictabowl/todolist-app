package it.aldinucci.todoapp.adapter.in.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import it.aldinucci.todoapp.application.port.in.DeleteProjectByIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@Controller
@RequestMapping("/web/project/{projectId}")
public class DeleteProjectWebController {

	private InputModelAuthorization<ProjectIdDTO> authorize;
	private DeleteProjectByIdUsePort deleteProject;
	
	
	@Autowired
	public DeleteProjectWebController(InputModelAuthorization<ProjectIdDTO> authorize,
			DeleteProjectByIdUsePort deleteProject) {
		super();
		this.authorize = authorize;
		this.deleteProject = deleteProject;
	}


	@DeleteMapping
	public String deleteProjectWebEndpoint(Authentication authentication, @PathVariable ProjectIdDTO projectId) {
		authorize.check(authentication.getName(), projectId);
		deleteProject.delete(projectId);
		return "redirect:/web";
	}
}
