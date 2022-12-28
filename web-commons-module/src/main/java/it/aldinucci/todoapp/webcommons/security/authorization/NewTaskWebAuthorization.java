package it.aldinucci.todoapp.webcommons.security.authorization;

import java.util.Optional;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.in.LoadUserByProjectIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;
import it.aldinucci.todoapp.webcommons.exception.ForbiddenWebAccessException;

@Component
public class NewTaskWebAuthorization implements InputModelAuthorization<NewTaskDTOIn> {

	private LoadUserByProjectIdUsePort loadUserService;
	
	public NewTaskWebAuthorization(LoadUserByProjectIdUsePort loadUserService){
		this.loadUserService = loadUserService;
	}

	@Override
	public void check(UserIdDTO userId, NewTaskDTOIn model) 
			throws ForbiddenWebAccessException, AppProjectNotFoundException {
		Optional<User> user = loadUserService.load(new ProjectIdDTO(model.getProjectId()));
		if (user.isEmpty())
			throw new AppProjectNotFoundException("Could not find Project with id: "+model.getProjectId());
		if (!userId.getId().equals(user.get().getEmail()))
			throw new ForbiddenWebAccessException("Operation not authorized for the autheticated user");
	}
	
}
