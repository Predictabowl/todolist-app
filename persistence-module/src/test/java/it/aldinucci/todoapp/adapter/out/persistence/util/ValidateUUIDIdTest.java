package it.aldinucci.todoapp.adapter.out.persistence.util;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValidateUUIDIdTest {

	private ValidateUUIDId validator;
	
	@BeforeEach
	void setUp() {
		validator = new ValidateUUIDId();
	}
	
	@Test
	void test_success() {
		UUID uuid = UUID.randomUUID();
		
		boolean valid = validator.isValid(uuid.toString());
		UUID id = validator.getId();
		
		assertThat(valid).isTrue();
		assertThat(id).isEqualTo(uuid);
		
		valid = validator.isValid("random 23");
		id = validator.getId();
		
		assertThat(valid).isFalse();
		assertThat(id).isNull();
	}
	
	@Test
	void test_fail() {
		boolean valid = validator.isValid("random 23");
		UUID id = validator.getId();
		
		assertThat(valid).isFalse();
		assertThat(id).isNull();
	}
	
	@Test
	void test_nullId() {
		boolean valid = validator.isValid(null);
		UUID id = validator.getId();
		
		assertThat(valid).isFalse();
		assertThat(id).isNull();
	}

}
