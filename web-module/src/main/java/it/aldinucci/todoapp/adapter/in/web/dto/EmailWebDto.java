package it.aldinucci.todoapp.adapter.in.web.dto;

import java.util.Objects;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class EmailWebDto {
	
	@NotNull
	@Email
	private String email;

	public EmailWebDto(@NotNull @Email String email) {
		super();
		this.email = email;
	}

	public final String getEmail() {
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
		EmailWebDto other = (EmailWebDto) obj;
		return Objects.equals(email, other.email);
	}

	@Override
	public String toString() {
		return "EmailWebDto [email=" + email + "]";
	}
	
	

}
