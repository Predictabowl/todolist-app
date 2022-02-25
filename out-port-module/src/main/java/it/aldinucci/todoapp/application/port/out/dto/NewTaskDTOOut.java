package it.aldinucci.todoapp.application.port.out.dto;

import java.util.Objects;

public class NewTaskDTOOut {

	private String name;
	private String description;
	private static final boolean COMPLETE = false;
	private Long projectId;
	
	public NewTaskDTOOut() {
	}

	public NewTaskDTOOut(String name, String description, Long projectId) {
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

	public static boolean isComplete() {
		return COMPLETE;
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
		NewTaskDTOOut other = (NewTaskDTOOut) obj;
		return Objects.equals(description, other.description) && Objects.equals(name, other.name)
				&& Objects.equals(projectId, other.projectId);
	}

	@Override
	public String toString() {
		return "NewTaskDTOOut [name=" + name + ", description=" + description + ", projectId=" + projectId + "]";
	}

}
