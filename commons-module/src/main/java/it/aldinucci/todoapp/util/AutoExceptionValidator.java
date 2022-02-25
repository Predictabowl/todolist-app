package it.aldinucci.todoapp.util;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;

public class AutoExceptionValidator<T> {

	private Validator validator;

	@Autowired
	public AutoExceptionValidator(Validator validator) {
		this.validator = validator;
	}
	
	public void validate(T dto) {
		Set<ConstraintViolation<T>> violations = validator.validate(dto);
		if(!violations.isEmpty())
			throw new ConstraintViolationException(violations);
	}
}
