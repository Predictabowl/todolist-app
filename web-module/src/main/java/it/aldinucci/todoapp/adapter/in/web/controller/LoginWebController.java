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
import org.springframework.web.servlet.ModelAndView;

import it.aldinucci.todoapp.adapter.in.web.dto.RegisterUserDto;
import it.aldinucci.todoapp.adapter.in.web.validator.RegisterUserValidator;
import it.aldinucci.todoapp.application.port.in.CreateUserUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewUserDTOIn;
import it.aldinucci.todoapp.exceptions.AppEmailAlreadyRegisteredException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Controller
@RequestMapping(BASE_WEB_URI)
public class LoginWebController{
	
	private static final String EMAIL_EXISTS = "emailExists";
	private CreateUserUsePort createUser;
	private AppGenericMapper<RegisterUserDto, NewUserDTOIn> mapper;
	private RegisterUserValidator registerUserValidator;

	@Autowired
	public LoginWebController(CreateUserUsePort createUser, AppGenericMapper<RegisterUserDto, NewUserDTOIn> mapper,
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
	public ModelAndView postRegistrationPage(@Valid RegisterUserDto newUser, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView("register");
		if(bindingResult.hasErrors()) 
			return modelAndView;
		
		try {
			createUser.create(mapper.map(newUser));
		} catch (AppEmailAlreadyRegisteredException e) {
			modelAndView.addObject(EMAIL_EXISTS, true);
			return modelAndView;
		}
		return new ModelAndView("redirect:/login");
	}

	@InitBinder(value = "registerUserDto")
	void initRegisterUserValidator(WebDataBinder binder) {
		binder.setValidator(registerUserValidator);
	}
}
