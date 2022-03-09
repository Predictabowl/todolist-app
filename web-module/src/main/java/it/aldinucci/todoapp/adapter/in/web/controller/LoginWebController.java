package it.aldinucci.todoapp.adapter.in.web.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginWebController{
	
	@GetMapping("/login")
	public String login() {
		return "login/login";
	}

}
