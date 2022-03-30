package it.aldinucci.todoapp.application.port.in.dto;

import java.util.Objects;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.aldinucci.todoapp.util.AutoValidatingInputModel;

public class UserDataDTOIn extends AutoValidatingInputModel<UserDataDTOIn>{

	@NotNull
	@NotEmpty
	@Size(max = 255)
	private final String username;

	public UserDataDTOIn(String username) {
		super();
		this.username = username;
		validateSelf();
	}

	public final String getUsername() {
		return username;
	}

	@Override
	public int hashCode() {
		return Objects.hash(username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDataDTOIn other = (UserDataDTOIn) obj;
		return Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "UserDataDTOIn [username=" + username + "]";
	}
	
}
