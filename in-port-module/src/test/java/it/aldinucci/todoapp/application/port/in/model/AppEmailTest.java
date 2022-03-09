package it.aldinucci.todoapp.application.port.in.model;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;

class AppEmailTest {

	@Test
	void test_newEmailAreConvertedToLowercase() {
		AppEmail email = new AppEmail("Mail4Ever@tesT.it");
		
		assertThat(email.getEmail()).matches("mail4ever@test.it");
	}
	
	@Test
	void test_whenEmailNotWellFormed_shouldThrow() {
		assertThatThrownBy(() -> new AppEmail("malformed email"))
			.isInstanceOf(ConstraintViolationException.class);
	}
	
	@Test
	void test_emailIsEmpty_shouldThrow() {
		assertThatThrownBy(() -> new AppEmail(""))
			.isInstanceOf(ConstraintViolationException.class);
	}
	
	@Test
	void test_emailIsNullThrow() {
		assertThatThrownBy(() -> new AppEmail(null))
			.isInstanceOf(ConstraintViolationException.class);
	}
	
}
