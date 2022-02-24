package it.aldinucci.todoapp.application.port.in.dto;

import java.util.Objects;

import javax.validation.constraints.Positive;

import it.aldinucci.todoapp.util.AutoValidatingInputModel;

public class DeleteTaskDTOIn extends AutoValidatingInputModel<DeleteTaskDTOIn>{

	@Positive
	private Long taskId;

	public DeleteTaskDTOIn(Long taskId) {
		this.taskId = taskId;
		validateSelf();
	}

	public Long getTaskId() {
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
		DeleteTaskDTOIn other = (DeleteTaskDTOIn) obj;
		return Objects.equals(taskId, other.taskId);
	}

	@Override
	public String toString() {
		return "DeleteTaskDTOIn [taskId=" + taskId + "]";
	}

}
