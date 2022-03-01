package it.aldinucci.todoapp.application.port.out;

import it.aldinucci.todoapp.exceptions.AppProjectNotFoundException;

public interface DeleteProjectByIdDriverPort {

	public void delete (long id) throws AppProjectNotFoundException;
}
