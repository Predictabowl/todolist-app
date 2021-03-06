package it.aldinucci.todoapp.webcommons.security.authorization;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.in.LoadUserByTaskIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exception.AppTaskNotFoundException;
import it.aldinucci.todoapp.webcommons.exception.UnauthorizedWebAccessException;

@Component
public class TaskIdWebAuthorization implements InputModelAuthorization<TaskIdDTO> {

	private LoadUserByTaskIdUsePort loadUser;
	
	@Autowired
	public TaskIdWebAuthorization(LoadUserByTaskIdUsePort loadUser) {
		this.loadUser = loadUser;
	}

	@Override
	public void check(String authenticatedEmail, TaskIdDTO model) 
			throws UnauthorizedWebAccessException, AppTaskNotFoundException {
		Optional<User> user = loadUser.load(model);
		if(user.isEmpty())
			throw new AppTaskNotFoundException("Could not find Task with id: "+model.getTaskId());
		if(!authenticatedEmail.equals(user.get().getEmail()))
			throw new UnauthorizedWebAccessException("This operation is not permitted for the authenticated user");
	}

}
