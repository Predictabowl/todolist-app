package it.aldinucci.todoapp.adapter.out.persistence.util;


import static org.assertj.core.api.Assertions.assertThat;

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
		boolean valid = validateId.isValid("065");
		Long id = validateId.getId();
		
		assertThat(valid).isTrue();
		assertThat(id).isEqualTo(65L);
		
		valid = validateId.isValid("06a5");
		id = validateId.getId();
		
		assertThat(valid).isFalse();
		assertThat(id).isNull();
	}
	
	@Test
	void test_failure() {
		boolean valid = validateId.isValid("06a5");
		Long id = validateId.getId();
		
		assertThat(valid).isFalse();
		assertThat(id).isNull();
	}

}
