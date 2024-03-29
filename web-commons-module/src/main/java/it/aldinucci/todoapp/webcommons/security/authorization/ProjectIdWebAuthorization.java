package it.aldinucci.todoapp.webcommons.security.authorization;

import java.util.Optional;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.in.LoadUserByProjectIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;
import it.aldinucci.todoapp.webcommons.exception.ForbiddenWebAccessException;

@Component
public class ProjectIdWebAuthorization implements InputModelAuthorization<ProjectIdDTO> {

	private LoadUserByProjectIdUsePort loadUser;

	public ProjectIdWebAuthorization(LoadUserByProjectIdUsePort loadUser) {
		this.loadUser = loadUser;
	}

	@Override
	public void check(UserIdDTO userId, ProjectIdDTO model)
			throws ForbiddenWebAccessException, AppProjectNotFoundException {
		Optional<User> user = loadUser.load(model);
		if (user.isEmpty())
			throw new AppProjectNotFoundException("Could not find Project with id: " + model.getProjectId());
		if (!userId.getId().equals(user.get().getEmail()))
			throw new ForbiddenWebAccessException("This operation is not permitted for the authenticated user");
	}

}
