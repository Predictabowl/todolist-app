package it.aldinucci.todoapp.application.port.in.dto;

import java.util.Objects;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class NewTaskDTOIn {

	@NotEmpty
	private String name;
	
	@NotNull
	private String description;
	
	@Positive
	@NotNull
	private Long projectId;
	
	public NewTaskDTOIn() {
	}

	public NewTaskDTOIn(String name, String description, Long projectId) {
		super();
		this.name = name;
		this.description = description;
		this.projectId = projectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
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
		NewTaskDTOIn other = (NewTaskDTOIn) obj;
		return Objects.equals(description, other.description) && Objects.equals(name, other.name)
				&& Objects.equals(projectId, other.projectId);
	}

	@Override
	public String toString() {
		return "NewTaskDTOIn [name=" + name + ", description=" + description + ", projectId=" + projectId + "]";
	}

	
}
