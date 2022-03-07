package it.aldinucci.todoapp.adapter.in.web.validator;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.aldinucci.todoapp.adapter.in.web.dto.RegisterUserDto;

@Component
public class RegisterUserValidator implements Validator {

	private MessageSource messageSource;
	private Validator validator;

	@Autowired
	public RegisterUserValidator(MessageSource messageSource, Validator validator) {
		this.messageSource = messageSource;
		this.validator = validator;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return RegisterUserDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		validator.validate(target, errors);
		RegisterUserDto user = (RegisterUserDto) target;
		if (!user.getPassword().equals(user.getConfirmedPassword()))
			errors.rejectValue("confirmedPassword", messageSource.getMessage("registerUserDto.matchPasswords.error", null,
					Locale.getDefault()), "The confirmed password doesn't match");
	}

}
