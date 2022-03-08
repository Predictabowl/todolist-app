package it.aldinucci.todoapp.adapter.in.web.dto;

import java.util.Objects;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RegisterUserDto {

	@Email(message = "Email format not valid")
	@NotEmpty(message = "Email cannot be empty")
	private String email;

	@NotEmpty(message = "Username cannot be empty")
	@NotNull
	private String username;

	@NotEmpty(message = "Password cannot be empty")
	@NotNull
	private String password;

	@NotEmpty(message = "Confirmed password cannot be empty")
	@NotNull
	private String confirmedPassword;

	public RegisterUserDto() {
		this.email = "";
		this.username = "";
		this.password = "";
		this.confirmedPassword = "";
	}

	public RegisterUserDto(String email, String username, String password, String confirmedPassword) {
		this.email = email;
		this.username = username;
		this.password = password;
		this.confirmedPassword = confirmedPassword;
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

	public String getConfirmedPassword() {
		return confirmedPassword;
	}

	public void setConfirmedPassword(String confirmedPassword) {
		this.confirmedPassword = confirmedPassword;
	}

	@Override
	public int hashCode() {
		return Objects.hash(confirmedPassword, email, password, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegisterUserDto other = (RegisterUserDto) obj;
		return Objects.equals(confirmedPassword, other.confirmedPassword) && Objects.equals(email, other.email)
				&& Objects.equals(password, other.password) && Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "RegisterUserDto [email=" + email + ", username=" + username + ", password=" + password
				+ ", confirmedPassword=" + confirmedPassword + "]";
	}

}
