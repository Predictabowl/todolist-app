package it.aldinucci.todoapp.application.port.in.dto;

import java.util.Objects;

import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.domain.VerificationToken;

public class NewUserDtoOut {

	private User user;
	private VerificationToken token;
	
	
	public NewUserDtoOut(User user, VerificationToken token) {
		this.user = user;
		this.token = token;
	}


	public final User getUser() {
		return user;
	}


	public final VerificationToken getToken() {
		return token;
	}


	@Override
	public int hashCode() {
		return Objects.hash(token, user);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewUserDtoOut other = (NewUserDtoOut) obj;
		return Objects.equals(token, other.token) && Objects.equals(user, other.user);
	}


	@Override
	public String toString() {
		return "NewUserDtoOut [user=" + user + ", token=" + token + "]";
	}

}
