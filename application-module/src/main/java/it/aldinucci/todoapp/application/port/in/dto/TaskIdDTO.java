package it.aldinucci.todoapp.application.port.in.dto;

import java.util.Objects;

public class TaskIdDTO{

	private long taskId;

	public TaskIdDTO(long taskId) {
		this.taskId = taskId;
	}

	public long getTaskId() {
		return taskId;
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
		return taskId == other.taskId;
	}

	@Override
	public String toString() {
		return "DeleteTaskDTOIn [taskId=" + taskId + "]";
	}

}
