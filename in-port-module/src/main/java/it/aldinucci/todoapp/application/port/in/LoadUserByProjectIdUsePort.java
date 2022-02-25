package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.domain.User;

public interface LoadUserByProjectIdUsePort {

	public User load(ProjectIdDTO projectId);
}
