package it.aldinucci.todoapp.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.config.CommonsBeansProvider;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CommonsBeansProvider.class})
class AppPasswordEncoderImplTest {

	private AppPasswordEncoderImpl appEncoder;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Test
	void test_encode() {
		appEncoder = new AppPasswordEncoderImpl(encoder);
		String raw = "a string";
		
		String encoded = appEncoder.encode(raw);
		
		assertThat(encoder.matches(raw, encoded)).isTrue();
	}

	@Test
	void test_getEncoder() {
		appEncoder = new AppPasswordEncoderImpl(encoder);

		assertThat(appEncoder.getEncoder()).isSameAs(encoder);
	}
}
