package it.aldinucci.todoapp.application.port.in.dto;

import java.util.Objects;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.aldinucci.todoapp.application.port.in.model.AppEmail;
import it.aldinucci.todoapp.util.AutoValidatingInputModel;

public class NewUserDTOIn extends AutoValidatingInputModel<NewUserDTOIn>{
	
	@NotEmpty
	@NotNull
	@Size(max = 255)
	private final String username;
	
	private final AppEmail email;
	
	@NotEmpty
	@NotNull
	@Size(max = 255)
	private final String password;

	public NewUserDTOIn(String username, String email, String password) {
		this.username = username;
		this.email = new AppEmail(email);
		this.password = password;
		validateSelf();
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email.getEmail();
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
