package it.aldinucci.todoapp.util;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

class ValidationUtilsTest {

	@Test
	void test_empty() {
		String notUuid = "";
		
		assertThat(ValidationUtils.isValidUUID(notUuid)).isFalse();
	}
	
	@Test
	void test_notValid() {
		String notUuid = "random 2 numbers";
		
		assertThat(ValidationUtils.isValidUUID(notUuid)).isFalse();
	}
	
	@Test
	void test_Valid() {
		IntStream.range(0, 1000).mapToObj(i -> UUID.randomUUID().toString())
			.forEach(u -> assertThat(ValidationUtils.isValidUUID(u)).isTrue()); 
	}
	
	@Test
	void test_nullArgument() {
		assertThat(ValidationUtils.isValidUUID(null)).isFalse();
	}

}
