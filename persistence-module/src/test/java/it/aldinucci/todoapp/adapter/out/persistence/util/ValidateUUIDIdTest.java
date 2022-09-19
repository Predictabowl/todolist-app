package it.aldinucci.todoapp.adapter.out.persistence.util;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
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
		
		Optional<UUID> id = validator.getValidId(uuid.toString());
		
		assertThat(id).contains(uuid);
	}
	
	@Test
	void test_fail() {
		Optional<UUID> id = validator.getValidId("random 23");

		assertThat(id).isEmpty();
	}
	
	@Test
	void test_nullId() {
		Optional<UUID> id = validator.getValidId(null);
		
		assertThat(id).isEmpty();
	}

}
