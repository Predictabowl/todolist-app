package it.aldinucci.todoapp.webcommons.dto;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import it.aldinucci.todoapp.webcommons.dto.validator.annotation.MatchingPasswords;

public class RegisterUserDto {
	
		@Email
		@NotEmpty
		private final String email;
		
		@NotEmpty
		@NotNull
		@Size(max = 255)
		private final String username;
		
		@MatchingPasswords
		@Valid
		private final InputPasswordsDto passwords;
		
		public RegisterUserDto(String email, String username, String password, String confirmedPassword) {
			this.email = email;
			this.username = username;
			passwords = new InputPasswordsDto(password, confirmedPassword);
		}

		public final String getEmail() {
			return email;
		}

		public final String getUsername() {
			return username;
		}

		public final String getPassword() {
			return passwords.password();
		}
		
		public final String getConfirmedPassword() {
			return passwords.confirmedPassword();
		}

		@Override
		public int hashCode() {
			return Objects.hash(email, passwords, username);
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
			return Objects.equals(email, other.email) && Objects.equals(passwords, other.passwords)
					&& Objects.equals(username, other.username);
		}

		@Override
		public String toString() {
			return "RegisterUserDto [email=" + email + ", username=" + username + ", passwords=" + passwords + "]";
		}
		
}