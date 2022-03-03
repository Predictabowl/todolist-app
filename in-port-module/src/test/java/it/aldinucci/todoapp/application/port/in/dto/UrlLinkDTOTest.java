package it.aldinucci.todoapp.application.port.in.dto;


import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;

class UrlLinkDTOTest {

	@Test
	void test_whenLinkIsNull_shouldThrow() {
		assertThatThrownBy(() -> new UrlLinkDTO(null))
			.isInstanceOf(ConstraintViolationException.class); 
	}
	
	@Test
	void test_whenLinkIsEmpty_shouldThrow() {
		assertThatThrownBy(() -> new UrlLinkDTO(""))
			.isInstanceOf(ConstraintViolationException.class); 
	}
	
	@Test
	void test_whenLinkIsNotAvlid_shouldThrow() {
		assertThatThrownBy(() -> new UrlLinkDTO("Invalid-Address.com"))
			.isInstanceOf(ConstraintViolationException.class); 
	}

}
