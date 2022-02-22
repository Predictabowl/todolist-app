package it.aldinucci.todoapp.application.port.in.dto;

import java.util.Objects;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;


public class NewProjectDTOIn{

	@NotEmpty
	private String name;
	
	@Email
	private String userEmail;
	
	public NewProjectDTOIn() {
	}
	
	public NewProjectDTOIn(String name, String userEmail) {
		this.name = name;
		this.userEmail = userEmail;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, userEmail);
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
		return Objects.equals(name, other.name) && Objects.equals(userEmail, other.userEmail);
	}

	@Override
	public String toString() {
		return "NewProjectDTOIn [name=" + name + ", userEmail=" + userEmail + "]";
	}

}
