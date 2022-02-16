package it.aldinucci.todoapp.application.port.in;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import it.aldinucci.todoapp.util.AutoExceptionValidator;


public class NewProjectDTOIn{

	@NotNull
	private String name;
	
	@NotNull
	private String description;
	
	@NotNull
	private String userId;
	
	public NewProjectDTOIn(String name, String userId, String description) {
		this.name = name;
		this.description = description;
		this.userId = userId;
		validateSelf();
	}
	
	public NewProjectDTOIn(String name, String userId) {
		this.name = name;
		this.description = "";
		this.userId = userId;
		validateSelf();
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getUserId() {
		return userId;
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
		NewProjectDTOIn other = (NewProjectDTOIn) obj;
		return Objects.equals(description, other.description) && Objects.equals(name, other.name)
				&& Objects.equals(userId, other.userId);
	}

	@Override
	public String toString() {
		return "NewProjectDTO [name=" + name + ", description=" + description + ", userId=" + userId + "]";
	}
	
	private void validateSelf() {
		new AutoExceptionValidator<NewProjectDTOIn>().validate(this);
	}

}
