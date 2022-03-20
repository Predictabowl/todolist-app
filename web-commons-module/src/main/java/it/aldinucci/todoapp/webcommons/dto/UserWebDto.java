package it.aldinucci.todoapp.webcommons.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record UserWebDto (
	@NotNull @NotEmpty @Size(max = 255) String username,
	@NotNull @Email String email) {
}
