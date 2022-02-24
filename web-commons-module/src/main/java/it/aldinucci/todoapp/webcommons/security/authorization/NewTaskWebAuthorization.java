package it.aldinucci.todoapp.webcommons.security.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.in.LoadUserFromProjectIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.webcommons.exception.ForbiddenWebAccessException;

@Component
public class NewTaskWebAuthorization implements InputModelAuthorization<NewTaskDTOIn> {

	private LoadUserFromProjectIdUsePort loadUserService;
	
	@Autowired
	public NewTaskWebAuthorization(LoadUserFromProjectIdUsePort loadUserService){
		this.loadUserService = loadUserService;
	}

	@Override
	public void check(String authenticatedEmail, NewTaskDTOIn model) throws ForbiddenWebAccessException {
		User user = loadUserService.load(new ProjectIdDTO(model.getProjectId()));
		if (!authenticatedEmail.equals(user.getEmail()))
			throw new ForbiddenWebAccessException("Operation not authorized for the autheticated user");
	}
	
}
