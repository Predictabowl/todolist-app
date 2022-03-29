package it.aldinucci.todoapp.application.port.out;

import java.util.OptionalInt;

import it.aldinucci.todoapp.exception.AppProjectNotFoundException;

public interface GetTaskMaxOrderInProjectDriverPort {

	public OptionalInt get(String projectId) throws AppProjectNotFoundException;
}
