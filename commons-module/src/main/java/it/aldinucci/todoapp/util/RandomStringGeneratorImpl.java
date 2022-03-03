package it.aldinucci.todoapp.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class RandomStringGeneratorImpl implements RandomStringGenerator {

	@Override
	public String generate(int length) {
		return RandomStringUtils.random(length);
	}

}
