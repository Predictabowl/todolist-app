package it.aldinucci.todoapp.domain;

import java.util.Objects;

public class User {

	private String email;
	private String username;
	private String password;
	private boolean enabled;

	public User() {
		enabled = false;
	}

	public User(String email, String username, String password, boolean enabled) {
		this.email = email;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
	}

	public User(String email, String username, String password) {
		this.email = email;
		this.username = username;
		this.password = password;
		this.enabled = false;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
		User other = (User) obj;
		return Objects.equals(email, other.email) && enabled == other.enabled
				&& Objects.equals(password, other.password) && Objects.equals(username, other.username);
	}

	
}
