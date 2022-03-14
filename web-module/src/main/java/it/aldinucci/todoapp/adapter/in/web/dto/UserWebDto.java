package it.aldinucci.todoapp.adapter.in.web.dto;

import java.util.Objects;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserWebDto {
	
	@NotNull
	@NotEmpty
	@Size(max = 255)
	private String username;
	
	@NotNull
	@Email
	private String email;

	public UserWebDto() {
	}
	
	public UserWebDto(String username, String email) {
		this.username = username;
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserWebDto other = (UserWebDto) obj;
		return Objects.equals(email, other.email) && Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "UserWebDto [username=" + username + ", email=" + email + "]";
	}
	
	
}
