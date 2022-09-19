package it.aldinucci.todoapp.adapter.out.persistence.util;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValidateLongIdTest {

	private ValidateLongId validateId;
	
	@BeforeEach
	void setUp() {
		validateId = new ValidateLongId();
	}
	
	@Test
	void test_success() {
		Optional<Long> id = validateId.isValid("065");

		assertThat(id).contains(65L);
	}
	
	@Test
	void test_failure() {
		Optional<Long> id = validateId.isValid("06a5");
		
		assertThat(id).isEmpty();
	}
	
	@Test
	void test_nullId() {
		Optional<Long> id = validateId.isValid(null);
		
		assertThat(id).isEmpty();
	}

}
