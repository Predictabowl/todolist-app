package it.aldinucci.todoapp.application.port.in.dto;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;

class NewUserDTOInTest {

	@Test
	void test_valid() {
		assertThatCode(() -> new NewUserDTOIn("username", "email@email.it", "password"))
			.doesNotThrowAnyException();;
	}
	
	@Test
	void test_whenEmailNotWellFormed_shouldThrow() {
		assertThatThrownBy(() -> new NewUserDTOIn("username", "email", "password"))
			.isInstanceOf(ConstraintViolationException.class);
	}
	
	@Test
	void test_whenUsernameIsEmpty_shouldThrow() {
		assertThatThrownBy(() -> new NewUserDTOIn("", "email@rmail.it", "password"))
			.isInstanceOf(ConstraintViolationException.class);
	}
	
	@Test
	void test_whenPasswordIsEmpty_shouldThrow() {
		assertThatThrownBy(() -> new NewUserDTOIn("username", "email@email.it", ""))
			.isInstanceOf(ConstraintViolationException.class);
	}

}
