package it.aldinucci.todoapp.adapter.in.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.aldinucci.todoapp.application.port.in.LoadUserByEmailUsePort;
import it.aldinucci.todoapp.application.port.in.VerifyUserEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.VerifyTokenDTOIn;

@Controller
@RequestMapping("/user/register")
public class UserRegisterVerificationController {

	private VerifyUserEmailUsePort verifyUser;
	private LoadUserByEmailUsePort loadUser;
	
	
	@Autowired
	public UserRegisterVerificationController(VerifyUserEmailUsePort verifyUser, LoadUserByEmailUsePort loadUSer) {
		this.verifyUser = verifyUser;
		this.loadUser = loadUSer;
	}

	@GetMapping("/resend/verification")
	public String resendVerificationToken(Model model) {
		model.addAttribute("actionLink", "/user/register/resend/verification");
		return "email-form";
	}
	
	@PostMapping("/resend/verification")
	public String resendVerificationToken(@Valid UserIdDTO userIdDTO) {
		loadUser.load(userIdDTO);
		return "redirect:/login";
	}
	
	@GetMapping("/verification/{token}")
	public String confirmRegistration(@Valid VerifyTokenDTOIn token) {
		verifyUser.verify(token);
		return "redirect:/login";
	}
}
