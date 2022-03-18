package it.aldinucci.todoapp.adapter.in.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.aldinucci.todoapp.application.port.in.UpdateTaskUsePort;
import it.aldinucci.todoapp.application.port.in.dto.TaskDataDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.mapper.AppGenericMapper;
import it.aldinucci.todoapp.webcommons.dto.TaskDataWebDto;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@Controller
@RequestMapping("/web/project/{projectId}/task/{taskId}")
public class UpdateTaskWebController {

	private InputModelAuthorization<TaskIdDTO> authorize;
	private UpdateTaskUsePort updateTask;
	private AppGenericMapper<TaskDataWebDto, TaskDataDTOIn> mapper;
	
	@Autowired
	public UpdateTaskWebController(InputModelAuthorization<TaskIdDTO> authorize, UpdateTaskUsePort updateTask,
			AppGenericMapper<TaskDataWebDto, TaskDataDTOIn> mapper) {
		super();
		this.authorize = authorize;
		this.updateTask = updateTask;
		this.mapper = mapper;
	}


	@PutMapping
	public String updateTaskEndPoint(
				Authentication authentication,
				@PathVariable long projectId,
				@PathVariable long taskId,
				@Valid TaskDataWebDto taskData,
				BindingResult bindingResult) {
		
		if (bindingResult.hasErrors())
			return "redirect:/web/project/"+projectId+"/tasks";
		
		TaskIdDTO idDTO = new TaskIdDTO(taskId);
		authorize.check(authentication.getName(), idDTO);
		updateTask.update(idDTO, mapper.map(taskData));
		return "redirect:/web/project/"+projectId+"/tasks";
	}
	
}