package it.aldinucci.todoapp.application.port.in.model;

import java.util.Objects;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.aldinucci.todoapp.util.AutoValidatingInputModel;

public class AppPassword extends AutoValidatingInputModel<AppPassword>{

	@NotNull
	@NotEmpty
	@Size(max = 255)
	private final String password;

	public AppPassword(String email){
		this.password = email;
		validateSelf();
	}

	public final String getPassword() {
		return password;
	}

	@Override
	public int hashCode() {
		return Objects.hash(password);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppPassword other = (AppPassword) obj;
		return Objects.equals(password, other.password);
	}

	@Override
	public String toString() {
		return "AppPassword [password=" + password + "]";
	}

	
}