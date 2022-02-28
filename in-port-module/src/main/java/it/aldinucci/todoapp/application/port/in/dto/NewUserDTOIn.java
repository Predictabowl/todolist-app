package it.aldinucci.todoapp.application.port.in.dto;

import java.util.Objects;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import it.aldinucci.todoapp.util.AutoValidatingInputModel;

public class NewUserDTOIn extends AutoValidatingInputModel<NewUserDTOIn>{
	
	@NotEmpty
	private String username;
	
	@Email
	@NotNull
	private String email;
	
	@NotEmpty
	private String password;

	public NewUserDTOIn(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
		validateSelf();
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, password, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewUserDTOIn other = (NewUserDTOIn) obj;
		return Objects.equals(email, other.email) && Objects.equals(password, other.password)
				&& Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "NewUserDTOIn [username=" + username + ", email=" + email + ", password=" + password + "]";
	}

	
}
