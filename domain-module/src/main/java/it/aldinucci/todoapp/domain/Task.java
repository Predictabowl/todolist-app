package it.aldinucci.todoapp.domain;

import java.util.Objects;

public class Task {
	
	private Long id;
	private String name;
	private String description;
	private boolean completed;
	
	public Task() {
	}
	

	public Task(Long id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.completed = false;
	}
	
	public Task(Long id, String name, String description, boolean completed) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.completed = completed;
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


	@Override
	public int hashCode() {
		return Objects.hash(completed, description, id, name);
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
				&& Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}


	@Override
	public String toString() {
		return "Task [id=" + id + ", name=" + name + ", description=" + description + ", completed=" + completed + "]";
	}

}
