package it.aldinucci.todoapp.adapter.in.web.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.aldinucci.todoapp.adapter.in.web.dto.NewTaskWebDto;
import it.aldinucci.todoapp.adapter.in.web.dto.UserWebDto;
import it.aldinucci.todoapp.application.port.in.LoadProjectsByUserUsePort;
import it.aldinucci.todoapp.application.port.in.LoadTasksByProjectUsePort;
import it.aldinucci.todoapp.application.port.in.LoadUserByProjectIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@Controller
@RequestMapping("/project/{projectId}")
public class ProjectWebController {
	
	private LoadTasksByProjectUsePort loadTasks;
	private LoadUserByProjectIdUsePort loadUser;
	private LoadProjectsByUserUsePort loadProjects;
	private AppGenericMapper<User, UserWebDto> userMapper;
	private InputModelAuthorization<User> authorize;


	@Autowired
	public ProjectWebController(LoadTasksByProjectUsePort loadTasks, LoadUserByProjectIdUsePort loadUser,
			LoadProjectsByUserUsePort loadProjects, AppGenericMapper<User, UserWebDto> userMapper,
			InputModelAuthorization<User> authorize) {
		super();
		this.loadTasks = loadTasks;
		this.loadUser = loadUser;
		this.loadProjects = loadProjects;
		this.userMapper = userMapper;
		this.authorize = authorize;
	}

	@GetMapping("/tasks")
	public String getTasks(Authentication authentication, Model model, @Valid ProjectIdDTO projectId,
				NewTaskWebDto newTaskWebDto) {
		User user = loadUser.load(projectId);
		
		authorize.check(authentication.getName(), user);
		model.addAttribute("user", userMapper.map(user));
		List<Project> projects = loadProjects.load(new UserIdDTO(user.getEmail()));
		model.addAttribute("projects", projects);
		model.addAttribute("activeProject",	projects.stream()
				.filter(p -> p.getId().equals(projectId.getProjectId())).findFirst()
					.orElseThrow(() -> new AppProjectNotFoundException(
							"Critical Data Integrity error while searching project with id: "+projectId.getProjectId())));
		
		Map<Boolean, List<Task>> tasks = loadTasks.load(projectId).stream()
				.collect(Collectors.partitioningBy(Task::isCompleted));
		model.addAttribute("tasks",tasks.get(false));
		model.addAttribute("completedTasks",tasks.get(true));
		
		return "index";
	}
	

}
