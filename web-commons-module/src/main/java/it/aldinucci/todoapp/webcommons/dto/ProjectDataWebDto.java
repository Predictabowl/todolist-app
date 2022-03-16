package it.aldinucci.todoapp.webcommons.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record ProjectDataWebDto(
		long id,
		@NotEmpty @NotNull @Size(max = 255)	String name) {
}
