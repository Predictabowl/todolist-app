package it.aldinucci.todoapp.adapter.in.web.controller;

import static it.aldinucci.todoapp.adapter.in.web.util.AppLinksBuilder.buildVerificationLink;

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

import it.aldinucci.todoapp.adapter.in.web.dto.EmailWebDto;
import it.aldinucci.todoapp.application.port.in.RetrieveVerificationTokenUsePort;
import it.aldinucci.todoapp.application.port.in.SendVerificationEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.exceptions.AppUserEmailAlreadyVerifiedException;
import it.aldinucci.todoapp.exceptions.AppUserNotFoundException;

@Controller
@RequestMapping("/user/register/resend/verification")
public class ResendVerificationTokenController {
	
	private RetrieveVerificationTokenUsePort retrieveToken;
	private SendVerificationEmailUsePort sendMail;
	
	@Autowired
	public ResendVerificationTokenController(RetrieveVerificationTokenUsePort retrieveToken,
			SendVerificationEmailUsePort sendMail) {
		super();
		this.retrieveToken = retrieveToken;
		this.sendMail = sendMail;
	}

	@GetMapping
	public String getView(EmailWebDto emailWebDto, Model model) {
		model.addAttribute("actionLink", "/user/register/resend/verification");
		return "email.request";
	}
	
	@PostMapping
	public ModelAndView resendVerificationToken(@Valid EmailWebDto emailWebDto, BindingResult bindingResult ) {
		ModelAndView modelAndView = new ModelAndView("email.request");
		modelAndView.addObject("actionLink", "/user/register/resend/verification");
		if(bindingResult.hasErrors())
			return modelAndView;

		String token;
		try {
			token = retrieveToken.get(new UserIdDTO(emailWebDto.getEmail())).getToken();
		} catch (AppUserNotFoundException e) {
			modelAndView.addObject("emailNotFound", true);
			return modelAndView;
		}  catch (AppUserEmailAlreadyVerifiedException e) {
			modelAndView.addObject("emailAlreadyVerified", true);
			return modelAndView;
		}
		
		sendMail.send(buildVerificationLink(
				ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString(),
				token, emailWebDto.getEmail()));
		modelAndView.setViewName("redirect:/login");
		return modelAndView;
	}
	
}
