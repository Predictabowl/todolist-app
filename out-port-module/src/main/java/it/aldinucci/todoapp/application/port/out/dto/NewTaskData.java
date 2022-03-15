package it.aldinucci.todoapp.application.port.out.dto;

import java.util.Objects;

public class NewTaskData {

	private String name;
	private String description;
	private static final boolean COMPLETE = false;
	private int orderInProject;
	private Long projectId;
	
	public NewTaskData() {
		this.orderInProject = 0;
	}

	public NewTaskData(String name, String description, Long projectId, int orderInProject) {
		super();
		this.name = name;
		this.description = description;
		this.projectId = projectId;
		this.orderInProject = orderInProject;
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

	public final int getOrderInProject() {
		return orderInProject;
	}

	public final void setOrderInProject(int orderInProject) {
		this.orderInProject = orderInProject;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, name, orderInProject, projectId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewTaskData other = (NewTaskData) obj;
		return Objects.equals(description, other.description) && Objects.equals(name, other.name)
				&& orderInProject == other.orderInProject && Objects.equals(projectId, other.projectId);
	}

	@Override
	public String toString() {
		return "NewTaskData [name=" + name + ", description=" + description + ", orderInProject=" + orderInProject
				+ ", projectId=" + projectId + "]";
	}

}
