package it.aldinucci.todoapp.application.port.out.dto;

import java.util.Objects;

public class UserData {

	private String username;
	private String email;
	private String password;
	private boolean enabled;
	
	public UserData() {
	}

	public UserData(String username, String email, String password, boolean enabled) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, enabled, password, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserData other = (UserData) obj;
		return Objects.equals(email, other.email) && enabled == other.enabled
				&& Objects.equals(password, other.password) && Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "UserDTOOut [username=" + username + ", email=" + email + ", password=" + password + ", enabled="
				+ enabled + "]";
	}

}
