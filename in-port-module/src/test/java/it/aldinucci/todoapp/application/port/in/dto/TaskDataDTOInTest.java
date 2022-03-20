package it.aldinucci.todoapp.application.port.in.dto;


import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;

class TaskDataDTOInTest {

	@Test
	void test_noExceptions() {
		assertThatCode(() -> new TaskDataDTOIn("name", "description"))
			.doesNotThrowAnyException();
	}
	
	@Test
	void test_nameCannotBeNull() {
		assertThatThrownBy(() -> new TaskDataDTOIn(null, "description"))
			.isInstanceOf(ConstraintViolationException.class);
	}
	
	@Test
	void test_nameCannotBeEmpty() {
		assertThatThrownBy(() -> new TaskDataDTOIn("", "description"))
			.isInstanceOf(ConstraintViolationException.class);
	}
	
	@Test
	void test_nameCannotBeTooLong() {
		String longString = IntStream.range(0, 256)
				.mapToObj(i -> "a").collect(Collectors.joining());
		
		assertThatThrownBy(() -> new TaskDataDTOIn(longString, "description"))
			.isInstanceOf(ConstraintViolationException.class);
	}
	
	@Test
	void test_descriptionCannotBeNull() {
		assertThatThrownBy(() -> new TaskDataDTOIn("name", null))
			.isInstanceOf(ConstraintViolationException.class);
	}
	
	@Test
	void test_descriptionCannotBeEmpty() {
		assertThatThrownBy(() -> new TaskDataDTOIn("name", ""))
			.isInstanceOf(ConstraintViolationException.class);
	}
	
	@Test
	void test_descriptionCannotBeTooLong() {
		String longString = IntStream.range(0, 1025)
				.mapToObj(i -> "a").collect(Collectors.joining());
		
		assertThatThrownBy(() -> new TaskDataDTOIn("name", longString))
			.isInstanceOf(ConstraintViolationException.class);
	}

}
