package it.aldinucci.todoapp.webcommons.security.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadUserByProjectIdDriverPort;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.webcommons.exception.ForbiddenWebAccessException;

@Component
public class ProjectIdWebAuthorization implements InputModelAuthorization<ProjectIdDTO> {

	private LoadUserByProjectIdDriverPort loadUser;
	
	@Autowired
	public ProjectIdWebAuthorization(LoadUserByProjectIdDriverPort loadUser) {
		this.loadUser = loadUser;
	}

	@Override
	public void check(String authenticatedEmail, ProjectIdDTO model) throws ForbiddenWebAccessException {
		User user = loadUser.load(model.getProjectId());
		if(!authenticatedEmail.equals(user.getEmail()))
				throw new ForbiddenWebAccessException("This operation is not permitted for the authenticated user");
	}

}
