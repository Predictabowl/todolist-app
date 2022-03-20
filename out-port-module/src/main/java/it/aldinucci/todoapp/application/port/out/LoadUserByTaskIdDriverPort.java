package it.aldinucci.todoapp.application.port.out;

import java.util.Optional;

import it.aldinucci.todoapp.domain.User;

public interface LoadUserByTaskIdDriverPort {

	public Optional<User> load(long taskId);
}
