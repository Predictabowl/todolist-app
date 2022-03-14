package it.aldinucci.todoapp.application.port.out;

import it.aldinucci.todoapp.exception.AppTaskNotFoundException;

public interface DeleteTaskByIdDriverPort {

	public void delete(long id) throws AppTaskNotFoundException;
}
