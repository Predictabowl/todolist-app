package it.aldinucci.todoapp.webcommons.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record InputPasswordsDto(
		@NotNull @NotEmpty @Size(max= 255) String password, 
		@NotNull @NotEmpty @Size(max= 255) String confirmedPassword) {

}
