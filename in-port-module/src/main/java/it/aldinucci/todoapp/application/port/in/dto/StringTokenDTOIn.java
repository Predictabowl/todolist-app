package it.aldinucci.todoapp.application.port.in.dto;

import java.util.Objects;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import it.aldinucci.todoapp.util.AutoValidatingInputModel;

public class StringTokenDTOIn extends AutoValidatingInputModel<StringTokenDTOIn>{
	
	@NotNull
	@NotEmpty
	private final String token;

	public StringTokenDTOIn(String token) {
		this.token = token;
		validateSelf();
	}

	public String getToken() {
		return token;
	}

	@Override
	public int hashCode() {
		return Objects.hash(token);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StringTokenDTOIn other = (StringTokenDTOIn) obj;
		return Objects.equals(token, other.token);
	}

	@Override
	public String toString() {
		return "StringTokenDTOIn [token=" + token + "]";
	}
	
}
