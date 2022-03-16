package it.aldinucci.todoapp.webcommons.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public record EmailWebDto(@NotNull @Email String email) {
}
