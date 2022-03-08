package it.aldinucci.todoapp.application.port.out;

import java.util.Optional;

import it.aldinucci.todoapp.domain.User;

public interface LoadUserByEmailDriverPort {

	public Optional<User> load(String email);
}
