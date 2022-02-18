package it.aldinucci.todoapp.application.port.in.dto;

import java.util.Objects;

import javax.validation.constraints.Email;

public class UserIdDTO {

	@Email
	private String email;
	
	public UserIdDTO() {
	}

	public UserIdDTO(String email) {
		super();
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
