package it.aldinucci.todoapp.webcommons.dto;

import java.util.Objects;

import javax.validation.Valid;

import it.aldinucci.todoapp.webcommons.dto.validator.annotation.MatchingPasswords;
import it.aldinucci.todoapp.webcommons.model.InputPasswords;

public class InputPasswordsDto {

	@MatchingPasswords
	@Valid
	private final InputPasswords passwords;
	
	public InputPasswordsDto(String password, String confirmedPassword) {
		passwords = new InputPasswords(password, confirmedPassword);
	}
	
	public final String getPassword() {
		return passwords.password();
	}
	
	public final String getConfirmedPassword() {
		return passwords.confirmedPassword();
	}

	public final InputPasswords getPasswords() {
		return passwords;
	}

	@Override
	public int hashCode() {
		return Objects.hash(passwords);
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InputPasswordsDto other = (InputPasswordsDto) obj;
		return Objects.equals(passwords, other.passwords);
	}

	@Override
	public String toString() {
		return "InputPasswordsDto [passwords=" + passwords + "]";
	}
	
}
