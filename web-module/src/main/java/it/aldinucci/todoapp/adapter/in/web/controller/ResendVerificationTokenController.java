package it.aldinucci.todoapp.adapter.in.web.controller;

import static it.aldinucci.todoapp.adapter.in.web.util.AppLinksBuilder.buildVerificationLink;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import it.aldinucci.todoapp.application.port.in.GetOrCreateVerificationTokenUsePort;
import it.aldinucci.todoapp.application.port.in.SendVerificationEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exception.AppUserEmailAlreadyVerifiedException;
import it.aldinucci.todoapp.webcommons.dto.EmailWebDto;

@Controller
@RequestMapping("/user/register/resend/verification")
public class ResendVerificationTokenController {
	
	private static final String EMAIL_REQUEST_VIEW = "login/email.request";
	
	private final GetOrCreateVerificationTokenUsePort retrieveToken;
	private final SendVerificationEmailUsePort sendMail;
	
	public ResendVerificationTokenController(GetOrCreateVerificationTokenUsePort retrieveToken,
			SendVerificationEmailUsePort sendMail) {
		super();
		this.retrieveToken = retrieveToken;
		this.sendMail = sendMail;
	}

	@GetMapping
	public String getView(EmailWebDto emailWebDto, Model model) {
		model.addAttribute("actionLink", "/user/register/resend/verification");
		return EMAIL_REQUEST_VIEW;
	}
	
	@PostMapping
	public ModelAndView resendVerificationToken(@Valid EmailWebDto emailWebDto, BindingResult bindingResult ) {
		ModelAndView modelAndView = new ModelAndView(EMAIL_REQUEST_VIEW);
		modelAndView.addObject("actionLink", "/user/register/resend/verification");
		if(bindingResult.hasErrors())
			return modelAndView;

		Optional<VerificationToken> optionalToken;
		try {
			optionalToken = retrieveToken.get(new UserIdDTO(emailWebDto.email()));
			if (optionalToken.isEmpty()) {
				modelAndView.addObject("emailNotFound", true);
				return modelAndView;
			}
		}  catch (AppUserEmailAlreadyVerifiedException e) {
			modelAndView.addObject("emailAlreadyVerified", true);
			return modelAndView;
		}
		
		String token = optionalToken.get().getToken();
		sendMail.send(buildVerificationLink(
				ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString(),
				token, emailWebDto.email()));
		modelAndView.setViewName("login/register.sent.verification");
		modelAndView.addObject("useremail", emailWebDto.email());
		return modelAndView;
	}
	
}
