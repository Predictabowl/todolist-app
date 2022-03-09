package it.aldinucci.todoapp.adapter.in.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/project")
public class ProjectWebController {

	@GetMapping("/{projectId}")
	public String getProjectView(@PathVariable long projectId, Model model) {
		return "project";
	}
	
}
