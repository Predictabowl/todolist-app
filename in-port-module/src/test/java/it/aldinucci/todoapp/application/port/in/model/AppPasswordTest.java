package it.aldinucci.todoapp.application.port.in.model;


import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;

class AppPasswordTest {

	@Test
	void test_passwordIsEmpty_shouldThrow() {
		assertThatThrownBy(() -> new AppPassword(""))
			.isInstanceOf(ConstraintViolationException.class);
	}
	
	@Test
	void test_passwordIsNullThrow() {
		assertThatThrownBy(() -> new AppPassword(null))
			.isInstanceOf(ConstraintViolationException.class);
	}
	
	@Test
	void test_passwordExceedingMaxLength_shouldThrow() {
		String longString = IntStream.range(0, 256)
				.mapToObj(i -> "a").collect(Collectors.joining());
		
		assertThatThrownBy(() -> new AppPassword(longString))
			.isInstanceOf(ConstraintViolationException.class);
	}
	
}
