package it.aldinucci.todoapp.application.port.in.dto;

import java.util.Objects;

import it.aldinucci.todoapp.application.port.in.model.StringId;

public class ProjectIdDTO{

	private final StringId projectId;

	public ProjectIdDTO(String projectId) {
		super();
		this.projectId = new StringId(projectId);
	}

	public final String getProjectId() {
		return projectId.getId();
	}

	@Override
	public int hashCode() {
		return Objects.hash(projectId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjectIdDTO other = (ProjectIdDTO) obj;
		return Objects.equals(projectId, other.projectId);
	}

	@Override
	public String toString() {
		return "ProjectIdDTO [projectId=" + projectId + "]";
	}
	
}