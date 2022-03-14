package it.aldinucci.todoapp.application.port.in.dto;


import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

	@Test
	void test_nameExceedingMaxLength_shouldThrow() {
		String longName = IntStream.range(0, 256)
				.mapToObj(i -> "a").collect(Collectors.joining());
		
		assertThatThrownBy(() -> new NewProjectDTOIn(longName, "user@email.it"))
			.isInstanceOf(ConstraintViolationException.class);
	}

}
