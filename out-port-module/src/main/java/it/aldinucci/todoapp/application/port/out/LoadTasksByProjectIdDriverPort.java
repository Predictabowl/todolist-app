package it.aldinucci.todoapp.application.port.out;

import java.util.List;

import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;

public interface LoadTasksByProjectIdDriverPort {

	public List<Task> load(String projectId) throws AppProjectNotFoundException;
}
