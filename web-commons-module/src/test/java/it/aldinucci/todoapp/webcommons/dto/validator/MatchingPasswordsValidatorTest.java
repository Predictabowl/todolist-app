package it.aldinucci.todoapp.webcommons.dto.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.MockitoAnnotations.openMocks;

import javax.validation.ConstraintValidatorContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import it.aldinucci.todoapp.webcommons.model.InputPasswords;

class MatchingPasswordsValidatorTest {

	private MatchingPasswordsValidator matchingValidator;
	
	@Mock
	private ConstraintValidatorContext context;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
		matchingValidator = new MatchingPasswordsValidator();
	}
	
	@Test
	void test_validate_success() {
		InputPasswords passwords = new InputPasswords("pass", "pass");
		
		boolean valid = matchingValidator.isValid(passwords, context);
		
		assertThat(valid).isTrue();
		verifyNoInteractions(context);
	}
	
	@Test
	void test_validate_failure() {
		InputPasswords passwords = new InputPasswords("pass 1", "pass 2");
		
		boolean valid = matchingValidator.isValid(passwords, context);
		
		assertThat(valid).isFalse();
		verifyNoInteractions(context);
	}

}
