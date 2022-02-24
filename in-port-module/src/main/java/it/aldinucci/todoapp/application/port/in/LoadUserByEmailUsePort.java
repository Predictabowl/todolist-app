package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.User;

public interface LoadUserByEmailUsePort {

	public User load(UserIdDTO id);
}
