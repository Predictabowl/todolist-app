package it.aldinucci.todoapp.application.port.out.dto;

import java.util.Objects;


public class NewProjectDTOOut{

	private String name;
	private String description;
	private String userEmail;
	
	public NewProjectDTOOut() {
	}
	
	public NewProjectDTOOut(String name, String userEmail, String description) {
		this.name = name;
		this.description = description;
		this.userEmail = userEmail;
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

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, name, userEmail);
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
				&& Objects.equals(userEmail, other.userEmail);
	}

	@Override
	public String toString() {
		return "NewProjectDTOOut [name=" + name + ", description=" + description + ", userEmail=" + userEmail + "]";
	}

	
}
