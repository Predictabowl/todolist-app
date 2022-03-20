package it.aldinucci.todoapp.webcommons.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record RegisterUserDto (
	@Email @NotEmpty String email,
	@NotEmpty @NotNull @Size(max = 255) String username,
	@NotEmpty @NotNull @Size(max = 255) String password,
	@NotEmpty @NotNull @Size(max = 255) String confirmedPassword) {
}