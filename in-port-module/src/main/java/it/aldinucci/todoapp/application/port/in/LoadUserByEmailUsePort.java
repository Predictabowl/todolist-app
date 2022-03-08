package it.aldinucci.todoapp.application.port.in;

import java.util.Optional;

import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.User;

public interface LoadUserByEmailUsePort {

	public Optional<User> load(UserIdDTO id);
}
