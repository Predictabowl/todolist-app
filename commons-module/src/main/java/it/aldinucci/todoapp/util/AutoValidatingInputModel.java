package it.aldinucci.todoapp.util;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

public abstract class AutoValidatingInputModel<T> {

	@SuppressWarnings("unchecked")
	protected void validateSelf() {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<T>> violations = validator.validate((T)this);
		if(!violations.isEmpty())
			throw new ConstraintViolationException(violations);
	}
	
}
