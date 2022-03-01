package it.aldinucci.todoapp.application.port.in.dto;

import java.util.Objects;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import it.aldinucci.todoapp.util.AutoValidatingInputModel;

public class UserIdDTO extends AutoValidatingInputModel<UserIdDTO>{

	@Email
	@NotNull
	private String email;
	
	public UserIdDTO(String email) {
		this.email = email;
		validateSelf();
	}

	public String getEmail() {
		return email;
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
