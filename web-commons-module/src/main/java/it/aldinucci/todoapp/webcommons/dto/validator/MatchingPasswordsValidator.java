package it.aldinucci.todoapp.webcommons.dto.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import it.aldinucci.todoapp.webcommons.dto.InputPasswordsDto;
import it.aldinucci.todoapp.webcommons.dto.validator.annotation.MatchingPasswords;

public class MatchingPasswordsValidator implements ConstraintValidator<MatchingPasswords, InputPasswordsDto>{

	@Override
	public boolean isValid(InputPasswordsDto value, ConstraintValidatorContext context) {
		return value.password().equals(value.confirmedPassword());
	}

}
