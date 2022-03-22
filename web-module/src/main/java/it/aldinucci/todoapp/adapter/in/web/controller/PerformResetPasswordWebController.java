package it.aldinucci.todoapp.adapter.in.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user/register/password/reset/perform")
public class PerformResetPasswordWebController {

	@GetMapping
	public String requestNewPassword() {
		return null;
	}
	
	@PutMapping
	public ModelAndView updatePassword() {
		return null;
	}
}
