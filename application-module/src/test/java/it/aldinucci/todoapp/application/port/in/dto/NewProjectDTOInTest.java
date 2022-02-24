package it.aldinucci.todoapp.application.port.in.dto;


import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;

class NewProjectDTOInTest {

	@Test
	void test_whenEmailNotWellFormed_shouldThrow() {
		assertThatThrownBy(() -> new NewProjectDTOIn("username", "malformed email"))
			.isInstanceOf(ConstraintViolationException.class);
	}
	
	@Test
	void test_whenNameIsEmpty_shouldThrow() {
		assertThatThrownBy(() -> new NewProjectDTOIn("", "user@email.it"))
			.isInstanceOf(ConstraintViolationException.class);
	}


}
