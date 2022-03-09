package it.aldinucci.todoapp.adapter.in.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.aldinucci.todoapp.application.port.in.LoadTasksByProjectUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.domain.Task;

@Controller
@RequestMapping("/project/{projectId}")
public class TaskWebController {
	
	private static final String MESSAGE_ATTRIBUTE = "message";
	
	private LoadTasksByProjectUsePort loadTasks;
	
	
	@Autowired
	public TaskWebController(LoadTasksByProjectUsePort loadTasks) {
		super();
		this.loadTasks = loadTasks;
	}



	@GetMapping("/tasks")
	public String tasks(Authentication authentication, Model model, @Valid ProjectIdDTO projectId) {
		List<Task> tasks = loadTasks.load(projectId);
		model.addAttribute("tasks",tasks);
		model.addAttribute(MESSAGE_ATTRIBUTE,tasks.isEmpty() ? "No tasks" : "");
		
		return "project";
	}
}
