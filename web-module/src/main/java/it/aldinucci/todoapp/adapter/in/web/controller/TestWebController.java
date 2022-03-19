package it.aldinucci.todoapp.adapter.in.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user/register/test")
public class TestWebController {

	@GetMapping
	public String testPage(Model model) {
		model.addAttribute("testo", "tutti al\r\nMare");
		return "test";
	}
	
	@PostMapping
	public ModelAndView postPage(@RequestParam String posted) {
		ModelAndView model = new ModelAndView();
		model.setViewName("test");
		model.addObject("testo", "");
		model.addObject("posted", posted);
		return model;
	}
	
}
