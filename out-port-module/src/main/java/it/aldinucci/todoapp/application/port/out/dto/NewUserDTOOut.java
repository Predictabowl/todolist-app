package it.aldinucci.todoapp.application.port.out.dto;

import java.util.Objects;

public class NewUserDTOOut {
	
	private String username;
	private String email;
	private String password;
	
	public NewUserDTOOut() {
	}

	public NewUserDTOOut(String username, String email, String password) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
		NewUserDTOOut other = (NewUserDTOOut) obj;
		return Objects.equals(email, other.email) && Objects.equals(password, other.password)
				&& Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "NewUserDTOOut [username=" + username + ", email=" + email + ", password=" + password + "]";
	}
	
}
