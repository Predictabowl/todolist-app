package it.aldinucci.todoapp.adapter.in.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestWebController {

	@GetMapping("/user/register/test")
	public String testPage() {
		return "test";
	}
	
}
