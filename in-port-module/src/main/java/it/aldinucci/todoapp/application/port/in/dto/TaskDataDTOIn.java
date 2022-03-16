package it.aldinucci.todoapp.application.port.in.dto;

import java.util.Objects;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.aldinucci.todoapp.util.AutoValidatingInputModel;

public class TaskDataDTOIn extends AutoValidatingInputModel<TaskDataDTOIn>{

	@NotNull
	@NotEmpty
	@Size(max = 255)
	private final String name;

	@NotNull
	@NotEmpty
	@Size(max = 1024)
	private final String description;

	public TaskDataDTOIn(String name, String description) {
		super();
		this.name = name;
		this.description = description;
		validateSelf();
	}

	public final String getName() {
		return name;
	}

	public final String getDescription() {
		return description;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskDataDTOIn other = (TaskDataDTOIn) obj;
		return Objects.equals(description, other.description) && Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "TaskDataDTOIn [name=" + name + ", description=" + description + "]";
	}
	
}
