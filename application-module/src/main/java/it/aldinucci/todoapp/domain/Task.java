package it.aldinucci.todoapp.domain;

import java.util.Objects;

public class Task {
	
	private Long id;
	private String name;
	private String description;
	private boolean completed;
	private Project project;
	
	public Task(Long id, String name, String description, Project project) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.completed = false;
		this.project = project;
	}
	
	public Task(Long id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.completed = false;
		this.project = null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
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
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", name=" + name + ", description=" + description + ", completed=" + completed + "]";
	}
	
	
}
