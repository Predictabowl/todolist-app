package it.aldinucci.todoapp.util;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

public class AutoExceptionValidator<T> {

	private Validator validator;

	public AutoExceptionValidator() {
		this.validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	public void validate(T dto) {
		Set<ConstraintViolation<T>> violations = validator.validate(dto);
		if(!violations.isEmpty())
			throw new ConstraintViolationException(violations);
	}
}
