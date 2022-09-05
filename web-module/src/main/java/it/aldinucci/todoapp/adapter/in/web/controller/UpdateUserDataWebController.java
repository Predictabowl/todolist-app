package it.aldinucci.todoapp.adapter.in.web.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import it.aldinucci.todoapp.application.port.in.LoadUserByIdUsePort;
import it.aldinucci.todoapp.application.port.in.UpdateUserDataUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserDataDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;
import it.aldinucci.todoapp.webcommons.dto.UserDataWebDto;

@Controller
@RequestMapping("/web/user/data")
public class UpdateUserDataWebController {

	private final LoadUserByIdUsePort loadUser;
	private final UpdateUserDataUsePort updateUser;
	
	@Autowired
	public UpdateUserDataWebController(LoadUserByIdUsePort loadUser, UpdateUserDataUsePort updateUser) {
		super();
		this.loadUser = loadUser;
		this.updateUser = updateUser;
	}

	@GetMapping
	public String getUserDataPage(Authentication authentication, Model model) {
		Optional<User> loaded = loadUser.load(new UserIdDTO(authentication.getName()));
		if (loaded.isEmpty())
			throw new AppUserNotFoundException("Could not find user with email: "+authentication.getName());
		
		User user = loaded.get();
		model.addAttribute("userDataWebDto", new UserDataWebDto(user.getUsername()));
		return "user.settings";
	}
	
	@PostMapping
	public ModelAndView postUserDataUpdate(Authentication authentication, 
				@Valid UserDataWebDto userDataWebDto, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView("user.settings");
		if(bindingResult.hasErrors())
			return modelAndView;
		
		Optional<User> optional = updateUser.update(
				new UserIdDTO(authentication.getName()), 
				new UserDataDTOIn(userDataWebDto.username()));
		if(optional.isEmpty())
			throw new AppUserNotFoundException("Could not find user with email: "+authentication.getName());
		
		return modelAndView;
	}
}
