package it.aldinucci.todoapp.application.port.in.dto;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;

class NewTaskDTOInTest {

	
	@Test
	void test_whenCorrect_shouldNotThrow() {
		assertThatCode(() -> new NewTaskDTOIn("name", "description", "3"))
			.doesNotThrowAnyException();;
	}
	
	@Test
	void test_whenDescriptionNull_shouldThrow() {
		assertThatThrownBy(() -> new NewTaskDTOIn("name", null, "3"))
			.isInstanceOf(ConstraintViolationException.class);
	}
	
	@Test
	void test_whenNameNull_shouldThrow() {
		assertThatThrownBy(() -> new NewTaskDTOIn(null, "description", "3"))
			.isInstanceOf(ConstraintViolationException.class);
	}
	
	@Test
	void test_whenNameEmpty_shouldThrow() {
		assertThatThrownBy(() -> new NewTaskDTOIn("", "description", "3"))
			.isInstanceOf(ConstraintViolationException.class);
	}
	
	@Test
	void test_descriptionExceedingMaxLength_shouldThrow() {
		String longDescription = IntStream.range(0, 1025)
				.mapToObj(i -> "a").collect(Collectors.joining());
		
		assertThatThrownBy(() -> new NewTaskDTOIn("name", longDescription ,"3"))
			.isInstanceOf(ConstraintViolationException.class);
	}
	
	@Test
	void test_nameExceedingMaxLength_shouldThrow() {
		String longName = IntStream.range(0, 256)
				.mapToObj(i -> "a").collect(Collectors.joining());
		
		assertThatThrownBy(() -> new NewTaskDTOIn(longName, "description" ,"3"))
			.isInstanceOf(ConstraintViolationException.class);
	}
}
