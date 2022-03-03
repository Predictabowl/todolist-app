package it.aldinucci.todoapp.application.port.in.dto;

import java.util.Objects;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import it.aldinucci.todoapp.util.AutoValidatingInputModel;

public class VerifyTokenDTOIn extends AutoValidatingInputModel<VerifyTokenDTOIn>{
	
	@NotNull
	@NotEmpty
	private String token;

	public VerifyTokenDTOIn(String token) {
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
		VerifyTokenDTOIn other = (VerifyTokenDTOIn) obj;
		return Objects.equals(token, other.token);
	}

	@Override
	public String toString() {
		return "VerifyTokenDTOIn [token=" + token + "]";
	}
	
}
