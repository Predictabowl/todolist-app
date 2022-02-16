package it.aldinucci.todoapp.application.port.out;

import java.util.Objects;


public class NewProjectDTOOut{

	private String name;
	private String description;
	private String userId;
	
	public NewProjectDTOOut() {
	}
	
	public NewProjectDTOOut(String name, String userId, String description) {
		this.name = name;
		this.description = description;
		this.userId = userId;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, name, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewProjectDTOOut other = (NewProjectDTOOut) obj;
		return Objects.equals(description, other.description) && Objects.equals(name, other.name)
				&& Objects.equals(userId, other.userId);
	}

	@Override
	public String toString() {
		return "NewProjectDTOOut [name=" + name + ", description=" + description + ", userId=" + userId + "]";
	}
	
	
}
