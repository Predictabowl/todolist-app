package it.aldinucci.todoapp.application.port.in.dto;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;

class UserDataDTOInTest {

	@Test
	void test_noExceptions() {
		assertThatCode(() -> new UserDataDTOIn("name"))
			.doesNotThrowAnyException();
	}
	
	@Test
	void test_nameCannotBeNull() {
		assertThatThrownBy(() -> new UserDataDTOIn(null))
			.isInstanceOf(ConstraintViolationException.class);
	}
	
	@Test
	void test_nameCannotBeEmpty() {
		assertThatThrownBy(() -> new UserDataDTOIn(""))
			.isInstanceOf(ConstraintViolationException.class);
	}
	
	@Test
	void test_nameCannotBeTooLong() {
		String longString = IntStream.range(0, 256)
				.mapToObj(i -> "a").collect(Collectors.joining());
		
		assertThatThrownBy(() -> new UserDataDTOIn(longString))
			.isInstanceOf(ConstraintViolationException.class);
	}

}
