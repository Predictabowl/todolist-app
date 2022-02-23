package it.aldinucci.todoapp.adapter.in.rest.security;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;

import it.aldinucci.todoapp.application.port.in.LoadUserFromProjectIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.domain.User;

public class NewTaskAuthorization implements AuthorizationManager<NewTaskDTOIn>{

	private LoadUserFromProjectIdUsePort loadUserService;
	
	@Autowired
	public NewTaskAuthorization(LoadUserFromProjectIdUsePort loadUserService) {
		this.loadUserService = loadUserService;
	}

	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, NewTaskDTOIn newTask) {
		User user = loadUserService.load(new ProjectIdDTO(newTask.getProjectId()));
		String authEmail = authentication.get().getName();
		if (authEmail.equals(user.getEmail()))
			return new AuthorizationDecision(true);
		return new AuthorizationDecision(false);
	}

}
