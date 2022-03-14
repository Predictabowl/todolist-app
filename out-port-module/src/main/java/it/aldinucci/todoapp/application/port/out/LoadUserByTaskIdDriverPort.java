package it.aldinucci.todoapp.application.port.out;

import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exception.AppTaskNotFoundException;

public interface LoadUserByTaskIdDriverPort {

	public User load(long taskId) throws AppTaskNotFoundException;
}
