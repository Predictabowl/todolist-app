package it.aldinucci.todoapp.application.port.in.dto;

import java.util.Objects;

import it.aldinucci.todoapp.application.port.in.model.StringId;

public class TaskIdDTO {

	private final StringId taskId;

	public TaskIdDTO(String taskId) {
		super();
		this.taskId = new StringId(taskId);
	}

	public final String getTaskId() {
		return taskId.getId();
	}

	@Override
	public int hashCode() {
		return Objects.hash(taskId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskIdDTO other = (TaskIdDTO) obj;
		return Objects.equals(taskId, other.taskId);
	}

	@Override
	public String toString() {
		return "TaskIdDTO [taskId=" + taskId + "]";
	}
	
}
