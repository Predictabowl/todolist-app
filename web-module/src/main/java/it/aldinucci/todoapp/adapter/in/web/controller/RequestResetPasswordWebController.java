package it.aldinucci.todoapp.adapter.in.web.controller;

import static it.aldinucci.todoapp.adapter.in.web.util.AppLinksBuilder.buildResetPasswordLink;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import it.aldinucci.todoapp.application.port.in.GetOrCreatePasswordResetTokenUsePort;
import it.aldinucci.todoapp.application.port.in.SendResetPasswordEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.ResetPasswordToken;
import it.aldinucci.todoapp.webcommons.dto.EmailWebDto;

@Controller
@RequestMapping("/user/register/password/reset")
public class RequestResetPasswordWebController {

	private static final String BASE_URL = "/user/register/password/reset";
	private static final String EMAIL_REQUEST_VIEW = "login/email.request";
	
	private final GetOrCreatePasswordResetTokenUsePort retrieveToken;
	private final SendResetPasswordEmailUsePort sendEmail;
	

	@Autowired
	public RequestResetPasswordWebController(GetOrCreatePasswordResetTokenUsePort retrieveToken,
			SendResetPasswordEmailUsePort sendEmail) {
		super();
		this.retrieveToken = retrieveToken;
		this.sendEmail = sendEmail;
	}

	@GetMapping
	public String emailRequest(EmailWebDto email, Model model) {
		model.addAttribute("actionLink", BASE_URL);
		return EMAIL_REQUEST_VIEW;
	}
	
	@PostMapping
	public ModelAndView tokenCreation(@Valid EmailWebDto email, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView(EMAIL_REQUEST_VIEW);
		modelAndView.addObject("actionLink", BASE_URL);
		if(bindingResult.hasErrors())
			return modelAndView;
		
		Optional<ResetPasswordToken> token = retrieveToken.get(new UserIdDTO(email.email()));
		if(token.isEmpty()) {
			modelAndView.addObject("emailNotFound", true);
			return modelAndView;
		}
		
		sendEmail.send(buildResetPasswordLink(
				ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString(),
				token.get().getToken(), 
				email.email()));
		
		modelAndView.clear();
		modelAndView.addObject("useremail", email.email());
		modelAndView.setViewName("login/register.sent.verification");
		return modelAndView;
	}
}
