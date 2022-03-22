package it.aldinucci.todoapp.webcommons.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record TaskDataWebDto(
		@NotNull @NotEmpty @Size(max = 255) String name,
		@NotNull @Size(max = 1024) String description) {
}
