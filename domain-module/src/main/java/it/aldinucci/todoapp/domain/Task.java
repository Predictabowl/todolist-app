package it.aldinucci.todoapp.domain;

import java.util.Objects;

public class Task {

	private String id;
	private String name;
	private String description;
	private boolean completed;
	private int orderInProject;

	public Task() {
		this.orderInProject = 0;
	}

	public Task(String id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.completed = false;
		this.orderInProject = 0;
	}

	public Task(String id, String name, String description, boolean completed) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.completed = completed;
		this.orderInProject = 0;
	}

	public Task(String id, String name, String description, boolean completed, int orderInProject) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.completed = completed;
		this.orderInProject = orderInProject;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public final int getOrderInProject() {
		return orderInProject;
	}

	public final void setOrderInProject(int orderInProject) {
		this.orderInProject = orderInProject;
	}

	@Override
	public int hashCode() {
		return Objects.hash(completed, description, id, name, orderInProject);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		return completed == other.completed && Objects.equals(description, other.description)
				&& Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& orderInProject == other.orderInProject;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", name=" + name + ", description=" + description + ", completed=" + completed
				+ ", orderInProject=" + orderInProject + "]";
	}

}
