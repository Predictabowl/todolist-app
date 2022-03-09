package it.aldinucci.todoapp.adapter.in.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.aldinucci.todoapp.adapter.in.web.dto.UserWebDto;
import it.aldinucci.todoapp.application.port.in.LoadProjectsByUserUsePort;
import it.aldinucci.todoapp.application.port.in.LoadUserByEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exceptions.AppUserNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Controller
@RequestMapping("/")
public class IndexWebController {

	private static final String MESSAGE_ATTRIBUTE = "message";
	
	private LoadProjectsByUserUsePort loadProjects;
	private LoadUserByEmailUsePort loadUser;
	private AppGenericMapper<User, UserWebDto> mapper;

	@Autowired
	public IndexWebController(LoadProjectsByUserUsePort loadProjects, LoadUserByEmailUsePort loadUser,
			AppGenericMapper<User, UserWebDto> mapper) {
		this.loadProjects = loadProjects;
		this.loadUser = loadUser;
		this.mapper = mapper;
	}

	@GetMapping
	public String index(Authentication authentication, Model model) {
		UserIdDTO userId = new UserIdDTO(authentication.getName());
		List<Project> projects = loadProjects.load(userId);
		model.addAttribute("projects", projects);
		model.addAttribute("user", mapper.map(
				loadUser.load(userId).orElseThrow(() -> 
					new AppUserNotFoundException("Critical error: could not find user with email: "+userId.getEmail()))));
		model.addAttribute(MESSAGE_ATTRIBUTE, projects.isEmpty() ? "No project" : "");

		return "index";
	}

}
