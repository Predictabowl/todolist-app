package it.aldinucci.todoapp.adapter.in.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.aldinucci.todoapp.application.port.in.LoadProjectsByUserUsePort;
import it.aldinucci.todoapp.application.port.in.LoadUserByIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;
import it.aldinucci.todoapp.webcommons.dto.UserWebDto;

@Controller
@RequestMapping
public class IndexWebController {

	private final LoadProjectsByUserUsePort loadProjects;
	private final LoadUserByIdUsePort loadUser;
	private final AppGenericMapper<User, UserWebDto> mapper;

	@Autowired
	public IndexWebController(LoadProjectsByUserUsePort loadProjects, LoadUserByIdUsePort loadUser,
			AppGenericMapper<User, UserWebDto> mapper) {
		this.loadProjects = loadProjects;
		this.loadUser = loadUser;
		this.mapper = mapper;
	}

	@GetMapping("/web")
	public String index(Authentication authentication, Model model) {
		UserIdDTO userId = new UserIdDTO(authentication.getName());
		model.addAttribute("projects", loadProjects.load(userId));
		model.addAttribute("user", mapper.map(
				loadUser.load(userId).orElseThrow(() -> 
					new AppUserNotFoundException("Critical error: could not find user with email: "+userId.getId()))));

		return "index";
	}
	
	@GetMapping("/")
	public String redirectHome() {
		return("redirect:/web");
	}

}
