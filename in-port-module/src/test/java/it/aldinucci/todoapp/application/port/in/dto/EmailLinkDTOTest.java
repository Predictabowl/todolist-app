package it.aldinucci.todoapp.application.port.in.dto;


import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;

class EmailLinkDTOTest {
	
	private static final String FIXTURE_URL = "http://localhost:8080/";
	private static final String FIXTURE_EMAIL= "user@email.it";

	@Test
	void test_dtoWellFormed() {
		assertThatCode(() -> new EmailLinkDTO(FIXTURE_URL, FIXTURE_EMAIL))
			.doesNotThrowAnyException();; 
	}
	
	@Test
	void test_whenLinkIsNull_shouldThrow() {
		assertThatThrownBy(() -> new EmailLinkDTO(null,FIXTURE_EMAIL))
			.isInstanceOf(ConstraintViolationException.class); 
	}
	
	@Test
	void test_whenLinkIsEmpty_shouldThrow() {
		assertThatThrownBy(() -> new EmailLinkDTO("",FIXTURE_EMAIL))
			.isInstanceOf(ConstraintViolationException.class); 
	}
	
	@Test
	void test_whenLinkIsNotAvlid_shouldThrow() {
		assertThatThrownBy(() -> new EmailLinkDTO("Invalid-Address.com",FIXTURE_EMAIL))
			.isInstanceOf(ConstraintViolationException.class); 
	}
	
	@Test
	void test_whenEmailIsNull_shouldThrow() {
		assertThatThrownBy(() -> new EmailLinkDTO(FIXTURE_URL,null))
			.isInstanceOf(ConstraintViolationException.class); 
	}
	
	@Test
	void test_whenEmailIsEmpty_shouldThrow() {
		assertThatThrownBy(() -> new EmailLinkDTO(FIXTURE_URL,""))
			.isInstanceOf(ConstraintViolationException.class); 
	}
	
	@Test
	void test_whenEmailIsNotWellFormed_shouldThrow() {
		assertThatThrownBy(() -> new EmailLinkDTO(FIXTURE_URL,"malformed.it"))
			.isInstanceOf(ConstraintViolationException.class); 
	}

}
