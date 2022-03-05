package it.aldinucci.todoapp.application.service.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class TokenStringGeneratorImplTest {
	
	private static final int NUM_STRINGS = 200;
	
	@Test
	void test_RandomString_shouldBeAlphanumeric() {
		TokenStringGeneratorImpl rsGen = new TokenStringGeneratorImpl();
		Set<String> strings = new HashSet<>(); 
		
		for(int i=0; i<NUM_STRINGS; i++) {
			strings.add(rsGen.generate());
		}

		assertThat(strings).hasSize(NUM_STRINGS);
		assertThat(strings.stream().allMatch(s -> s.matches("[0-9,a-z,A-Z,-]*"))).isTrue();
	}
	
}
