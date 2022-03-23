package it.aldinucci.todoapp.application.port.in.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;

class StringTokenDTOInTest {

	@Test
	void test_whenTokenNull_shouldthrow() {
		assertThatThrownBy(() -> new StringTokenDTOIn(null))
			.isInstanceOf(ConstraintViolationException.class);
	}
	
	@Test
	void test_whenTokenEmpty_shouldthrow() {
		assertThatThrownBy(() -> new StringTokenDTOIn(""))
			.isInstanceOf(ConstraintViolationException.class);
	}

}
