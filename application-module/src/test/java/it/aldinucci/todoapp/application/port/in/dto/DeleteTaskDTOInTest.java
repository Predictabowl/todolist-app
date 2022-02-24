package it.aldinucci.todoapp.application.port.in.dto;

import static org.assertj.core.api.Assertions.*;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;

class DeleteTaskDTOInTest {

	@Test
	void test_idNotPositive_shouldThrow() {
		assertThatThrownBy(() -> new DeleteTaskDTOIn(0L))
			.isInstanceOf(ConstraintViolationException.class);
	}

}
