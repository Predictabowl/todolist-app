package it.aldinucci.todoapp.adapter.in.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.aldinucci.todoapp.application.port.in.VerifyUserEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.VerifyTokenDTOIn;

@Controller
@RequestMapping("/user/register/verification")
public class UserRegisterVerificationController {

	private VerifyUserEmailUsePort verifyUser;

	@Autowired
	public UserRegisterVerificationController(VerifyUserEmailUsePort verifyUser) {
		super();
		this.verifyUser = verifyUser;
	}

	@GetMapping("/{token}")
	public String confirmRegistration(@Valid VerifyTokenDTOIn token) {
		verifyUser.verify(token);
		return "redirect:/login";
	}
}
