package it.aldinucci.todoapp.application.port.out;

import it.aldinucci.todoapp.exception.AppProjectNotFoundException;

public interface DeleteProjectByIdDriverPort {

	public void delete (long id) throws AppProjectNotFoundException;
}
