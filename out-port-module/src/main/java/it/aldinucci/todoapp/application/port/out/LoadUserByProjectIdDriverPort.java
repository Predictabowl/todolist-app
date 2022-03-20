package it.aldinucci.todoapp.application.port.out;

import java.util.Optional;

import it.aldinucci.todoapp.domain.User;

public interface LoadUserByProjectIdDriverPort {

	public Optional<User> load(long projectId);
}
