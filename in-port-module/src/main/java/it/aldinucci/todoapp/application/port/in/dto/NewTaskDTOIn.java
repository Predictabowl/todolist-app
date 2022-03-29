package it.aldinucci.todoapp.application.port.in.dto;

import java.util.Objects;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.aldinucci.todoapp.application.port.in.model.StringId;
import it.aldinucci.todoapp.util.AutoValidatingInputModel;

public class NewTaskDTOIn extends AutoValidatingInputModel<NewTaskDTOIn>{

	@NotEmpty
	@NotNull
	@Size(max = 255)
	private final String name;
	
	@NotNull
	@Size(max = 1024)
	private final String description;
	
	private final StringId projectId;
	
	public NewTaskDTOIn(String name, String description, String projectId) {
		this.name = name;
		this.description = description;
		this.projectId = new StringId(projectId);
		validateSelf();
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getProjectId() {
		return projectId.getId();
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
