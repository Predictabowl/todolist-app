package it.aldinucci.todoapp.application.port.in.dto;

import java.util.Objects;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


public class NewProjectDTOIn{

	@NotEmpty
	private String name;
	
	@NotNull
	private String description;
	
	@Email
	private String userEmail;
	
	public NewProjectDTOIn() {
	}
	
	public NewProjectDTOIn(String name, String userEmail, String description) {
		this.name = name;
		this.description = description;
		this.userEmail = userEmail;
	}
	
	public NewProjectDTOIn(String name, String userEmail) {
		this.name = name;
		this.description = "";
		this.userEmail = userEmail;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	public void setName(String name) {
		this.name = name;
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
		NewProjectDTOIn other = (NewProjectDTOIn) obj;
		return Objects.equals(description, other.description) && Objects.equals(name, other.name)
				&& Objects.equals(userEmail, other.userEmail);
	}

	@Override
	public String toString() {
		return "NewProjectDTOIn [name=" + name + ", description=" + description + ", userEmail=" + userEmail + "]";
	}

	
}
