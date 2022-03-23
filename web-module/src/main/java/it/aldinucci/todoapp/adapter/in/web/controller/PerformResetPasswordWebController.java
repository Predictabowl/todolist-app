package it.aldinucci.todoapp.adapter.in.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import it.aldinucci.todoapp.application.port.in.ChangeUserPasswordUsePort;
import it.aldinucci.todoapp.application.port.in.VerifyResetPasswordTokenUsePort;
import it.aldinucci.todoapp.application.port.in.dto.StringTokenDTOIn;
import it.aldinucci.todoapp.application.port.in.model.AppPassword;
import it.aldinucci.todoapp.webcommons.dto.InputPasswordsDto;

@Controller
@RequestMapping("/user/register/password/reset/perform/{token}")
public class PerformResetPasswordWebController {

	private static final String REQUEST_PASSWORD_VIEW = "login/password.request";
	private static final String BASE_URL = "/user/register/password/reset/perform";
	
	private VerifyResetPasswordTokenUsePort verifyToken;
	private ChangeUserPasswordUsePort changePassword;

	
	@Autowired
	public PerformResetPasswordWebController(VerifyResetPasswordTokenUsePort verifyToken,
			ChangeUserPasswordUsePort changePassword) {
		super();
		this.verifyToken = verifyToken;
		this.changePassword = changePassword;
	}

	@GetMapping
	public String requestNewPassword(@PathVariable String token, InputPasswordsDto inputPasswordsDto, Model model) {
		if (!verifyToken.verify(new StringTokenDTOIn(token)))
			return "redirect:/login";
		model.addAttribute("actionLink", BASE_URL+"/"+token);
		return REQUEST_PASSWORD_VIEW;
	}
	
	@PostMapping
	public ModelAndView updatePassword(@PathVariable String token, 
			@Valid InputPasswordsDto inputPasswordsDto,
			BindingResult bindingResult) {
		
		ModelAndView modelAndView = new ModelAndView("login/change.password.result");
		
		if (bindingResult.hasErrors()) {
			modelAndView.addObject("actionLink", BASE_URL+"/"+token);
			modelAndView.setViewName(REQUEST_PASSWORD_VIEW);
			return modelAndView;
		}
		
		if (changePassword.change(
				new StringTokenDTOIn(token), 
				new AppPassword(inputPasswordsDto.getPassword())))
			modelAndView.addObject("passwordChanged", true);
		
		return modelAndView;
	}
}
