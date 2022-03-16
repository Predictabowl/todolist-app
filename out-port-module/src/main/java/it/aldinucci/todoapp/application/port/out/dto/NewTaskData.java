package it.aldinucci.todoapp.application.port.out.dto;

public record NewTaskData(
	String name,
	String description,
	boolean complete,
	long projectId,
	int orderInProject) {
}
