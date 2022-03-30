package it.aldinucci.todoapp.webcommons.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record UserDataWebDto(@NotNull @NotEmpty String username) {
}
