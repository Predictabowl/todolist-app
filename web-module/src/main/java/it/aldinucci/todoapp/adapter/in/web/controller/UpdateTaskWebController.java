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
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.exception.AppTaskNotFoundException;
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
				@PathVariable String projectId,
				@PathVariable TaskIdDTO taskId,
				@Valid TaskDataWebDto taskData,
				BindingResult bindingResult) {
		
		if (bindingResult.hasErrors())
			return "redirect:/web/project/"+projectId+"/tasks";
		
		authorize.check(new UserIdDTO(authentication.getName()), taskId);
		if (updateTask.update(taskId, mapper.map(taskData)).isEmpty())
			throw new AppTaskNotFoundException("Could not find Task with id: "+taskId.getTaskId());
			
		return "redirect:/web/project/"+projectId+"/tasks";
	}
	
}
