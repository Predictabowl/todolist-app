package it.aldinucci.todoapp.application.port.in;

import static org.assertj.core.api.Assertions.*;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;

class NewProjectDTOValidationTest {

	@Test
	void test_newProjectWithNullNameShouldThrow() {
		assertThatThrownBy(() -> new NewProjectDTOIn(null, "userID"))
			.isInstanceOf(ConstraintViolationException.class);
	}
	
	@Test
	void test_newProjectWithNullUserShouldThrow() {
		assertThatThrownBy(() -> new NewProjectDTOIn("a project", null))
			.isInstanceOf(ConstraintViolationException.class);
	}
	
	@Test
	void test_newProjectDescriptionCannotBeNull() {
		assertThatThrownBy(() -> new NewProjectDTOIn("test project", "userID",null))
			.isInstanceOf(ConstraintViolationException.class);
	}

}
