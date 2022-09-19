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
		Optional<Long> id = validateId.getValidId("065");

		assertThat(id).contains(65L);
	}
	
	@Test
	void test_failure() {
		Optional<Long> id = validateId.getValidId("06a5");
		
		assertThat(id).isEmpty();
	}
	
	@Test
	void test_nullId() {
		Optional<Long> id = validateId.getValidId(null);
		
		assertThat(id).isEmpty();
	}

}
