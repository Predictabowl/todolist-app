package it.aldinucci.todoapp.application.port.in;

import java.util.List;

import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;

public interface LoadProjectsByUserUsePort {

	public List<Project> load(UserIdDTO userId) throws AppUserNotFoundException;
}
