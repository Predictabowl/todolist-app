package it.aldinucci.todoapp.application.port.in.model;

import java.util.Objects;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import it.aldinucci.todoapp.util.AutoValidatingInputModel;

public class AppEmail extends AutoValidatingInputModel<AppEmail>{

	@Email
	@NotNull
	@NotEmpty
	private String email;

	public AppEmail(String email){
		this.email = email;
		validateSelf();
		this.email = this.email.toLowerCase();
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
		AppEmail other = (AppEmail) obj;
		return Objects.equals(email, other.email);
	}

	@Override
	public String toString() {
		return "AppEmail [email=" + email + "]";
	}
	
}
