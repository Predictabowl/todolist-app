package it.aldinucci.todoapp.adapter.in.web.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import it.aldinucci.todoapp.application.port.in.LoadProjectsByUserUsePort;
import it.aldinucci.todoapp.application.port.in.LoadTasksByProjectUsePort;
import it.aldinucci.todoapp.application.port.in.LoadUserByProjectIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;
import it.aldinucci.todoapp.webcommons.dto.TaskDataWebDto;
import it.aldinucci.todoapp.webcommons.dto.UserWebDto;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@Controller
@RequestMapping("/web/project/{projectId}")
public class ProjectWebController {
	
	private final LoadTasksByProjectUsePort loadTasks;
	private final LoadUserByProjectIdUsePort loadUser;
	private final LoadProjectsByUserUsePort loadProjects;
	private final AppGenericMapper<User, UserWebDto> userMapper;
	private final InputModelAuthorization<User> authorize;


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
	public String getTasks(Authentication authentication,
				Model model,
				@PathVariable ProjectIdDTO projectId,
				TaskDataWebDto newTaskWebDto) {
		
		User user = loadUser.load(projectId).orElseThrow(() -> 
				new AppUserNotFoundException("Could not find User owner of project with id: "
						+projectId.getProjectId()));
		authorize.check(new UserIdDTO(authentication.getName()), user);
		
		model.addAttribute("user", userMapper.map(user));
		List<Project> projects = loadProjects.load(new UserIdDTO(user.getEmail()));
		model.addAttribute("projects", projects);
		model.addAttribute("activeProject",	projects.stream()
				.filter(p -> p.getId().equals(projectId.getProjectId())).findFirst()
					.orElseThrow(() -> new AppProjectNotFoundException(
							"Could not find project with id: "+projectId.getProjectId())));
		
		Map<Boolean, List<Task>> tasks = loadTasks.load(projectId).stream()
				.collect(Collectors.partitioningBy(Task::isCompleted));
		model.addAttribute("tasks",tasks.get(false));
		model.addAttribute("completedTasks",tasks.get(true));
		
		return "index";
	}
	

}
