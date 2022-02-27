package it.aldinucci.todoapp.webcommons.security.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.in.LoadUserByProjectIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.webcommons.exception.UnauthorizedWebAccessException;

@Component
public class ProjectIdWebAuthorization implements InputModelAuthorization<ProjectIdDTO> {

	private LoadUserByProjectIdUsePort loadUser;
	
	@Autowired
	public ProjectIdWebAuthorization(LoadUserByProjectIdUsePort loadUser) {
		this.loadUser = loadUser;
	}

	@Override
	public void check(String authenticatedEmail, ProjectIdDTO model) throws UnauthorizedWebAccessException {
		User user = loadUser.load(model);
		if(!authenticatedEmail.equals(user.getEmail()))
				throw new UnauthorizedWebAccessException("This operation is not permitted for the authenticated user");
	}

}
