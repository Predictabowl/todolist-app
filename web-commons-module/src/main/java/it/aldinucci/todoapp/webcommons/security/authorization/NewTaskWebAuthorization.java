package it.aldinucci.todoapp.webcommons.security.authorization;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.in.LoadUserByProjectIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;
import it.aldinucci.todoapp.webcommons.exception.UnauthorizedWebAccessException;

@Component
public class NewTaskWebAuthorization implements InputModelAuthorization<NewTaskDTOIn> {

	private LoadUserByProjectIdUsePort loadUserService;
	
	@Autowired
	public NewTaskWebAuthorization(LoadUserByProjectIdUsePort loadUserService){
		this.loadUserService = loadUserService;
	}

	@Override
	public void check(String authenticatedEmail, NewTaskDTOIn model) 
			throws UnauthorizedWebAccessException, AppProjectNotFoundException {
		Optional<User> user = loadUserService.load(new ProjectIdDTO(model.getProjectId()));
		if (user.isEmpty())
			throw new AppProjectNotFoundException("Could not find Project with id: "+model.getProjectId());
		if (!authenticatedEmail.equals(user.get().getEmail()))
			throw new UnauthorizedWebAccessException("Operation not authorized for the autheticated user");
	}
	
}
