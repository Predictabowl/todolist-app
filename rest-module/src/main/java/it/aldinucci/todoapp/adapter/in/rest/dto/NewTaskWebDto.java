package it.aldinucci.todoapp.adapter.in.rest.dto;

import java.util.Objects;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class NewTaskWebDto{

	@NotEmpty
	@NotNull
	private String name;
	
	@NotNull
	private String description;
	
	private long projectId;
	
	public NewTaskWebDto(String name, String description, long projectId) {
		this.name = name;
		this.description = description;
		this.projectId = projectId;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public long getProjectId() {
		return projectId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, name, projectId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewTaskWebDto other = (NewTaskWebDto) obj;
		return Objects.equals(description, other.description) && Objects.equals(name, other.name)
				&& projectId == other.projectId;
	}

	@Override
	public String toString() {
		return "NewTaskDTOIn [name=" + name + ", description=" + description + ", projectId=" + projectId + "]";
	}
	
}
