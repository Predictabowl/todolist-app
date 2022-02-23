package it.aldinucci.todoapp.application.port.in.dto;

import java.util.Objects;

import javax.validation.constraints.Positive;

public class DeleteTaskDTOIn {

	@Positive
	private Long taskId;
	
	public DeleteTaskDTOIn() {
	}

	public DeleteTaskDTOIn(Long taskId) {
		super();
		this.taskId = taskId;
		
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
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
		DeleteTaskDTOIn other = (DeleteTaskDTOIn) obj;
		return Objects.equals(taskId, other.taskId);
	}

	@Override
	public String toString() {
		return "DeleteTaskDTOIn [taskId=" + taskId + "]";
	}
	
}
