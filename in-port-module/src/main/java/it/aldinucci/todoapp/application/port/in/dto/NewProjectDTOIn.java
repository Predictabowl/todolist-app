package it.aldinucci.todoapp.application.port.in.dto;

import java.util.Objects;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.aldinucci.todoapp.application.port.in.model.AppEmail;
import it.aldinucci.todoapp.util.AutoValidatingInputModel;


public class NewProjectDTOIn extends AutoValidatingInputModel<NewProjectDTOIn>{

	@NotEmpty
	@NotNull
	@Size(max = 255)
	private final String name;
	
	private final AppEmail userEmail;
	
	public NewProjectDTOIn(String name, String userEmail) {
		this.name = name;
		this.userEmail = new AppEmail(userEmail);
		validateSelf();
	}
	
	public String getName() {
		return name;
	}

	public String getUserEmail() {
		return userEmail.getEmail();
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
