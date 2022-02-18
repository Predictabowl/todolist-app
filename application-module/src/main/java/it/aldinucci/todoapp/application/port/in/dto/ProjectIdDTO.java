package it.aldinucci.todoapp.application.port.in.dto;

import java.util.Objects;

import javax.validation.constraints.Positive;

public class ProjectIdDTO {

	@Positive
	private Long projectId;
	
	public ProjectIdDTO() {
	}

	public ProjectIdDTO(Long projectId) {
		super();
		this.projectId = projectId;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
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
