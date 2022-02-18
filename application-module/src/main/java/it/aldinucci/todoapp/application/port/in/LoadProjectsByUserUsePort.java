package it.aldinucci.todoapp.application.port.in;

import java.util.List;

import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.Project;

public interface LoadProjectsByUserUsePort {

	public List<Project> load(UserIdDTO userId);
}
