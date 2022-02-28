package it.aldinucci.todoapp.adapter.in.web.controller;

import static it.aldinucci.todoapp.webcommons.config.AppBaseURIs.BASE_WEB_URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.aldinucci.todoapp.adapter.in.web.dto.RegisterUserDto;
import it.aldinucci.todoapp.adapter.in.web.validator.RegisterUserValidator;
import it.aldinucci.todoapp.application.port.in.CreateUserUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewUserDTOIn;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Controller
@RequestMapping(BASE_WEB_URI)
public class SecurityWebController{
	
	private CreateUserUsePort createUser;
	private AppGenericMapper<RegisterUserDto, NewUserDTOIn> mapper;
	private RegisterUserValidator registerUserValidator;

	@Autowired
	public SecurityWebController(CreateUserUsePort createUser, AppGenericMapper<RegisterUserDto, NewUserDTOIn> mapper,
			RegisterUserValidator registerUserValidator) {
		this.createUser = createUser;
		this.mapper = mapper;
		this.registerUserValidator = registerUserValidator;
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/register")
	public String showRegistrationPage(RegisterUserDto registerUserDto) {
		return "register";
	}
	
	@PostMapping("/register")
	public String postRegistrationPage(@Valid RegisterUserDto newUser, BindingResult bindingResult) {
		if(bindingResult.hasErrors()){
			return "register";
		}
		createUser.create(mapper.map(newUser));
		return "redirect:/login";
	}
	
	@InitBinder(value = "registerUserDto")
	void initRegisterUserValidator(WebDataBinder binder) {
		binder.setValidator(registerUserValidator);
	}
}
