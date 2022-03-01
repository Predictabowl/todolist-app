package it.aldinucci.todoapp.application.port.out;

import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exceptions.AppProjectNotFoundException;

public interface LoadUserByProjectIdDriverPort {

	public User load(long projectId) throws AppProjectNotFoundException;
}
