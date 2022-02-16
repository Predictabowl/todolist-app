package it.aldinucci.todoapp.config;

import javax.validation.Validation;
import javax.validation.Validator;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonsBeansProviderConfig {
	
	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}

	@Bean
	public Validator getBeanValidator() {
		return Validation.buildDefaultValidatorFactory().getValidator();
	}
}
