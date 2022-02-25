package it.aldinucci.todoapp.application.port.out;

import it.aldinucci.todoapp.domain.User;

public interface LoadUserByTaskIdDriverPort {

	public User load(long taskId);
}
