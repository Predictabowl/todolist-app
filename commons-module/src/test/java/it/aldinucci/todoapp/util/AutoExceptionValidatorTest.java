package it.aldinucci.todoapp.util;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;
import java.util.function.Function;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;

import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AutoExceptionValidatorTest {

	private AutoExceptionValidator<ToBeValidated> validator;
	
	private class ToBeValidated{
		
		@NotNull
		private Object field;

		public ToBeValidated(@NotNull Object field) {
			this.field = field;
		}
	}
	
	@BeforeEach
	void setUp() {
		validator = new AutoExceptionValidator<>();
	}
	
	@Test
	void test_validatorWhenCorrectShouldNotThrowAnyExceptions() {
		ToBeValidated test1 = new ToBeValidated(new Object());
		
		assertThatNoException().isThrownBy(() -> validator.validate(test1));
	}
	
	@Test
	void test_validatorShouldThrowWhenConstraintsAreNotMet() {
		ToBeValidated test1 = new ToBeValidated(null);
		Validator sideValidator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<ToBeValidated>> violations = sideValidator.validate(test1);
		
		assertThatThrownBy(() -> validator.validate(test1))
			.isInstanceOf(ConstraintViolationException.class)
			.extracting(e -> ((ConstraintViolationException) e).getConstraintViolations())
			.isEqualTo(violations);
	}

}
