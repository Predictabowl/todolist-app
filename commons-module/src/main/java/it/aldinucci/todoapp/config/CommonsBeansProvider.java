package it.aldinucci.todoapp.config;

import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonsBeansProvider {

	@Bean
	public Validator getBeanValidator() {
		return Validation.buildDefaultValidatorFactory().getValidator();
	}

}
