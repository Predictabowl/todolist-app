package it.aldinucci.todoapp.application.port.out;

import it.aldinucci.todoapp.domain.User;

public interface LoadUserByEmailDriverPort {

	public User load(String email);
}
