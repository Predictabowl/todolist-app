package it.aldinucci.todoapp.webcommons.security.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.in.LoadUserByProjectIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.webcommons.exception.UnauthorizaedWebAccessException;

@Component
public class NewTaskWebAuthorization implements InputModelAuthorization<NewTaskDTOIn> {

	private LoadUserByProjectIdUsePort loadUserService;
	
	@Autowired
	public NewTaskWebAuthorization(LoadUserByProjectIdUsePort loadUserService){
		this.loadUserService = loadUserService;
	}

	@Override
	public void check(String authenticatedEmail, NewTaskDTOIn model) throws UnauthorizaedWebAccessException {
		User user = loadUserService.load(new ProjectIdDTO(model.getProjectId()));
		if (!authenticatedEmail.equals(user.getEmail()))
			throw new UnauthorizaedWebAccessException("Operation not authorized for the autheticated user");
	}
	
}
