package it.aldinucci.todoapp.application.port.out;

import it.aldinucci.todoapp.domain.User;

public interface LoadUserByProjectIdDriverPort {

	public User load(long projecId);
}
