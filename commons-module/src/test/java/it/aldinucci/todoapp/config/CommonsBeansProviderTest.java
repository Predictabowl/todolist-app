package it.aldinucci.todoapp.config;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import it.aldinucci.todoapp.util.AppPasswordEncoder;
import it.aldinucci.todoapp.util.AppPasswordEncoderImpl;

class CommonsBeansProviderTest {

	private CommonsBeansProvider sut;
	
	@BeforeEach
	void setUp() {
		sut = new CommonsBeansProvider();
	}
	
	@Test
	void test_getAppPasswordEncoder() {
		AppPasswordEncoder appPasswordEncoder = sut.getAppPasswordEncoder();
		
		assertThat(appPasswordEncoder).isInstanceOf(AppPasswordEncoderImpl.class);
		assertThat(appPasswordEncoder.getEncoder()).isInstanceOf(BCryptPasswordEncoder.class);
	}
	
	@Test
	void test_getPasswordEncoder() {
		assertThat(sut.getPasswordEncoder()).isInstanceOf(BCryptPasswordEncoder.class);
	}

}
