package it.aldinucci.todoapp.application.port.in.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;

class UserIdDTOTest {

	@Test
	void test_emailNotWellFormed_shouldThrow() {
		assertThatThrownBy(() -> new UserIdDTO("malformedemail"))
			.isInstanceOf(ConstraintViolationException.class);
	}

}
