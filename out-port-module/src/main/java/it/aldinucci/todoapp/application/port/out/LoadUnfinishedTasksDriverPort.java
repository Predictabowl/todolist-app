package it.aldinucci.todoapp.application.port.out;

import java.util.List;

import it.aldinucci.todoapp.domain.Task;

public interface LoadUnfinishedTasksDriverPort {

	public List<Task> load(long projectId);
}
