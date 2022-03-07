package it.aldinucci.todoapp.adapter.in.web.controller;

import static it.aldinucci.todoapp.adapter.in.web.util.AppLinksBuilder.buildVerificationLink;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import it.aldinucci.todoapp.adapter.in.web.dto.RegisterUserDto;
import it.aldinucci.todoapp.adapter.in.web.validator.RegisterUserValidator;
import it.aldinucci.todoapp.application.port.in.CreateUserUsePort;
import it.aldinucci.todoapp.application.port.in.SendVerificationEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewUserDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.NewUserDtoOut;
import it.aldinucci.todoapp.exceptions.AppEmailAlreadyRegisteredException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Controller
@RequestMapping("/user/register")
public class RegisterUserWebController {

	private static final String EMAIL_EXISTS = "emailExists";
	
	private CreateUserUsePort createUser;
	private AppGenericMapper<RegisterUserDto, NewUserDTOIn> mapper;
	private RegisterUserValidator registerUserValidator;
	private SendVerificationEmailUsePort sendVerificationEmail;
	
	@Autowired
	public RegisterUserWebController(CreateUserUsePort createUser,
			AppGenericMapper<RegisterUserDto, NewUserDTOIn> mapper, RegisterUserValidator registerUserValidator,
			SendVerificationEmailUsePort sendVerificationEmail) {
		super();
		this.createUser = createUser;
		this.mapper = mapper;
		this.registerUserValidator = registerUserValidator;
		this.sendVerificationEmail = sendVerificationEmail;
	}

	@GetMapping
	public String showRegistrationPage(RegisterUserDto registerUserDto) {
		return "register";
	}
	
	@PostMapping
	public ModelAndView postRegistrationPage(@Valid RegisterUserDto newUser, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView("register");
		if(bindingResult.hasErrors()) 
			return modelAndView;
		
		NewUserDtoOut user;
		try {
			user = createUser.create(mapper.map(newUser));
		} catch (AppEmailAlreadyRegisteredException e) {
			modelAndView.addObject(EMAIL_EXISTS, true);
			return modelAndView;
		}
		
		sendVerificationEmail.send(buildVerificationLink(
				ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString(),
				user.getToken().getToken(),
				user.getUser().getEmail()));
		modelAndView.setViewName("register.sent.verification");
		modelAndView.addObject("useremail", user.getUser().getEmail());
		return modelAndView;
	}

	@InitBinder(value = "registerUserDto")
	void initRegisterUserValidator(WebDataBinder binder) {
		binder.setValidator(registerUserValidator);
	}
}
