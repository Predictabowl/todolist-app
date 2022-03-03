package it.aldinucci.todoapp.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class RandomStringGeneratorImplTest {
	
	private static final int NUM_STRINGS = 20;
	private static final int LEN_STRINGS = 15;
	
	@Test
	void test_uncomplete_RandomString() {
		RandomStringGeneratorImpl rsGen = new RandomStringGeneratorImpl();
		Set<String> strings = new HashSet<>(); 
		
		for(int i=0; i<NUM_STRINGS; i++) {
			strings.add(rsGen.generate(LEN_STRINGS));
		}

		assertThat(strings).hasSize(NUM_STRINGS);
		assertThat(strings.stream().allMatch(s -> s.length() == LEN_STRINGS)).isTrue();
	}

}
