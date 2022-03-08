package it.aldinucci.todoapp.application.port.in.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;

class VerifyTokenDTOInTest {

	@Test
	void test_whenTokenNull_shouldthrow() {
		assertThatThrownBy(() -> new VerifyTokenDTOIn(null))
			.isInstanceOf(ConstraintViolationException.class);
	}
	
	@Test
	void test_whenTokenEmpty_shouldthrow() {
		assertThatThrownBy(() -> new VerifyTokenDTOIn(""))
			.isInstanceOf(ConstraintViolationException.class);
	}

}
