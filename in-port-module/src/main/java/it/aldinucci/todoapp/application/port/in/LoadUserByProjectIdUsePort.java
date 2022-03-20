package it.aldinucci.todoapp.application.port.in;

import java.util.Optional;

import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.domain.User;

public interface LoadUserByProjectIdUsePort {

	public Optional<User> load(ProjectIdDTO projectId);
}
