package it.aldinucci.todoapp.application.port.in.model;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;

class StringIdTest {

	@Test
	void test_validStringId() {
		assertThatCode(() -> new StringId("25862"))
			.doesNotThrowAnyException();
	}
	
	@Test
	void test_noLeadingZero() {
		assertThatThrownBy(() -> new StringId("025862"))
			.isInstanceOf(ConstraintViolationException.class);
	}
	
	@Test
	void test_invalidStringId() {
		assertThatThrownBy(() -> new StringId("something-2"))
			.isInstanceOf(ConstraintViolationException.class);
	}
	
	@Test
	void test_idIsEmpty_shouldThrow() {
		assertThatThrownBy(() -> new StringId(""))
			.isInstanceOf(ConstraintViolationException.class);
	}
	
	@Test
	void test_idIsNullThrow() {
		assertThatThrownBy(() -> new StringId(null))
			.isInstanceOf(ConstraintViolationException.class);
	}

}
