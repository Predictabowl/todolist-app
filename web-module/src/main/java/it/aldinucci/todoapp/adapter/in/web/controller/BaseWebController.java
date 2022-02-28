package it.aldinucci.todoapp.adapter.in.web.controller;

import static it.aldinucci.todoapp.webcommons.config.AppBaseURIs.BASE_WEB_URI;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.aldinucci.todoapp.adapter.in.web.dto.UserWebDto;
import it.aldinucci.todoapp.application.port.in.LoadProjectsByUserUsePort;
import it.aldinucci.todoapp.application.port.in.LoadTasksByProjectUsePort;
import it.aldinucci.todoapp.application.port.in.LoadUserByEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Controller
@RequestMapping(BASE_WEB_URI)
public class BaseWebController {
	
	private static final String MESSAGE_ATTRIBUTE = "message";
	private LoadProjectsByUserUsePort loadProjects;
	private LoadTasksByProjectUsePort loadTasks;
	private LoadUserByEmailUsePort loadUser;
	private AppGenericMapper<User, UserWebDto> mapper;
	
	@Autowired
	public BaseWebController(LoadProjectsByUserUsePort loadProjects, LoadTasksByProjectUsePort loadTasks,
			LoadUserByEmailUsePort loadUser, AppGenericMapper<User, UserWebDto> mapper) {
		this.loadProjects = loadProjects;
		this.loadTasks = loadTasks;
		this.loadUser = loadUser;
		this.mapper = mapper;
	}


	@GetMapping("/")
	public String index(Authentication authentication, Model model) {
		UserIdDTO userId = new UserIdDTO(authentication.getName());
		List<Project> projects = loadProjects.load(userId);
		model.addAttribute("projects",projects);
		model.addAttribute("user", mapper.map(loadUser.load(userId)));
		model.addAttribute(MESSAGE_ATTRIBUTE,projects.isEmpty() ? "No project" : "");
		
		return "index";
	}
	

	@GetMapping("/project/{projectId}/tasks")
	public String tasks(Authentication authentication, Model model, @Valid ProjectIdDTO projectId) {
		List<Task> tasks = loadTasks.load(projectId);
		model.addAttribute("tasks",tasks);
		model.addAttribute(MESSAGE_ATTRIBUTE,tasks.isEmpty() ? "No tasks" : "");
		
		return "project";
	}

}
