package it.aldinucci.todoapp.adapter.in.web.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.aldinucci.todoapp.adapter.in.web.dto.RegisterUserDto;

class RegisterUserValidatorTest {

	@Mock
	private MessageSource messageSource;
	
	@Mock
	private Validator validator;
	
	@Mock
	private Errors errors;
	
	@InjectMocks
	private RegisterUserValidator registerUserValidator;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
	}
	
	@Test
	void test_validatorSupportClassRegisterUserDto_isTrue() {
		RegisterUserDto user = new RegisterUserDto();
		assertThat(registerUserValidator.supports(user.getClass())).isTrue();
	}
	
	@Test
	void test_validatorDontSupportDifferentClasses() {
		assertThat(registerUserValidator.supports(Object.class)).isFalse();
	}
	
	@Test
	void test_validatorWhenPAsswordsDontMatch_shouldRejectValue() {
		when(messageSource.getMessage(isA(String.class), any(), isA(Locale.class)))
			.thenReturn("return message");
		
		RegisterUserDto user = new RegisterUserDto("test@email.it", "username", "password1", "password2");
		registerUserValidator.validate(user, errors);
		
		InOrder inOrder = Mockito.inOrder(messageSource,validator,errors);
		inOrder.verify(validator).validate(user, errors);
		inOrder.verify(messageSource).getMessage("registerUserDto.matchPasswords.error", null, Locale.getDefault());
		inOrder.verify(errors).rejectValue("confirmedPassword", "return message", "Both password should have the same value");
		verifyNoMoreInteractions(errors);
		verifyNoMoreInteractions(messageSource);
		verifyNoMoreInteractions(validator);
	}
	
	@Test
	void test_validatorWhenPAsswordsMatch_shouldNotRejectValue() {
		RegisterUserDto user = new RegisterUserDto("test@email.it", "username", "password", "password");
		registerUserValidator.validate(user, errors);
		
		verify(validator).validate(user, errors);
		verifyNoMoreInteractions(validator);
		verifyNoInteractions(messageSource);
		verifyNoInteractions(errors);
	}

}
