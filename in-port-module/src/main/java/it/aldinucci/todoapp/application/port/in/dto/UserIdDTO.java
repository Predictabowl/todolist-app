package it.aldinucci.todoapp.application.port.in.dto;

import java.util.Objects;

import it.aldinucci.todoapp.application.port.in.model.AppEmail;

public class UserIdDTO{

	private final AppEmail email;
	
	public UserIdDTO(String email) {
		this.email = new AppEmail(email);
	}

	public String getEmail() {
		return email.getEmail();
	}

	@Override
	public int hashCode() {
		return Objects.hash(email);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserIdDTO other = (UserIdDTO) obj;
		return Objects.equals(email, other.email);
	}

	@Override
	public String toString() {
		return "UserIdDTOIn [email=" + email + "]";
	}
	
	
}
