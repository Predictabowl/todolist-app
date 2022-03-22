package it.aldinucci.todoapp.webcommons.dto.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.MockitoAnnotations.openMocks;

import javax.validation.ConstraintValidatorContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import it.aldinucci.todoapp.webcommons.dto.InputPasswordsDto;

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
		InputPasswordsDto passwords = new InputPasswordsDto("pass", "pass");
		
		boolean valid = matchingValidator.isValid(passwords, context);
		
		assertThat(valid).isTrue();
		verifyNoInteractions(context);
	}
	
	@Test
	void test_validate_failure() {
		InputPasswordsDto passwords = new InputPasswordsDto("pass 1", "pass 2");
		
		boolean valid = matchingValidator.isValid(passwords, context);
		
		assertThat(valid).isFalse();
		verifyNoInteractions(context);
	}

}
