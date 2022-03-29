package it.aldinucci.todoapp.adapter.in.web.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.aldinucci.todoapp.application.port.in.UpdateProjectUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectDataDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;
import it.aldinucci.todoapp.webcommons.dto.ProjectDataWebDto;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@Controller
@RequestMapping("/web/project/{projectId}")
public class UpdateProjectWebController {

	private UpdateProjectUsePort updateProject;
	private InputModelAuthorization<ProjectIdDTO> authorize;
	private AppGenericMapper<ProjectDataWebDto, ProjectDataDTOIn> mapper;

	@Autowired
	public UpdateProjectWebController(UpdateProjectUsePort updateProject,
			InputModelAuthorization<ProjectIdDTO> authorize,
			AppGenericMapper<ProjectDataWebDto, ProjectDataDTOIn> mapper) {
		super();
		this.updateProject = updateProject;
		this.authorize = authorize;
		this.mapper = mapper;
	}


	@PutMapping
	public String updateProjectWebEndPoint(Authentication authentication, @PathVariable String projectId,
			@Valid ProjectDataWebDto projectData, BindingResult bindingResult) {
		if(bindingResult.hasErrors())
			return "redirect:/web/project/"+projectId+"/tasks";
		
		ProjectIdDTO idDTO = new ProjectIdDTO(projectId);
		authorize.check(authentication.getName(), idDTO);
		
		Optional<Project> optional = updateProject.update(idDTO, mapper.map(projectData));
		if (optional.isEmpty())
			throw new AppProjectNotFoundException("Project not found with id: "+projectId);
		
		return "redirect:/web/project/"+projectId+"/tasks";
	}
}
