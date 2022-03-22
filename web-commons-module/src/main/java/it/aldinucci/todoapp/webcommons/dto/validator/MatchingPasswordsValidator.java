package it.aldinucci.todoapp.webcommons.dto.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import it.aldinucci.todoapp.webcommons.dto.validator.annotation.MatchingPasswords;
import it.aldinucci.todoapp.webcommons.model.InputPasswords;

public class MatchingPasswordsValidator implements ConstraintValidator<MatchingPasswords, InputPasswords>{

	@Override
	public boolean isValid(InputPasswords value, ConstraintValidatorContext context) {
		return value.password().equals(value.confirmedPassword());
	}

}
