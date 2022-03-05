package it.aldinucci.todoapp.application.service.util;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class TokenStringGeneratorImpl implements TokenStringGenerator {

	@Override
	public String generate() {
		return UUID.randomUUID().toString();
	}

}
