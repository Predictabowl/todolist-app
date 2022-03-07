package it.aldinucci.todoapp.application.port.out.dto;

import java.util.Objects;


public class NewProjectData{

	private String name;
	private String userEmail;
	
	public NewProjectData() {
	}
	
	public NewProjectData(String name, String userEmail) {
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
		NewProjectData other = (NewProjectData) obj;
		return Objects.equals(name, other.name) && Objects.equals(userEmail, other.userEmail);
	}

	@Override
	public String toString() {
		return "NewProjectDTOOut [name=" + name + ", userEmail=" + userEmail + "]";
	}

	
}
