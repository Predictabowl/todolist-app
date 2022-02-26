package it.aldinucci.todoapp.application.port.in.dto;

import java.util.Objects;

public class ProjectIdDTO {

	private long projectId;
	
	public ProjectIdDTO(long projectId) {
		this.projectId = projectId;
	}

	public long getProjectId() {
		return projectId;
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
		return projectId == other.projectId;
	}

	@Override
	public String toString() {
		return "ProjectIdDTO [projectId=" + projectId + "]";
	}
	
}
