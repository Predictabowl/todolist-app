package it.aldinucci.todoapp.application.port.out;

import java.util.Optional;

import it.aldinucci.todoapp.domain.User;

public interface LoadUserByIdDriverPort {

	public Optional<User> load(String email);
}
