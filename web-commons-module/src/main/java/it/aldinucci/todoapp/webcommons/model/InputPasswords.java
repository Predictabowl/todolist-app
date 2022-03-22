package it.aldinucci.todoapp.webcommons.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record InputPasswords(
		@NotNull @Size(min = 5, max = 50) String password, 
		@NotNull @Size(min = 5, max = 50) String confirmedPassword) {

}
