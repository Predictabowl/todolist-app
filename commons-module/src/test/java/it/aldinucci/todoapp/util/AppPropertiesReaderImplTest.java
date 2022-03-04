package it.aldinucci.todoapp.util;

import static it.aldinucci.todoapp.util.ApplicationPropertyNames.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.exceptions.AppUnableToReadPropertyException;

@EnableConfigurationProperties
@PropertySource(value = "classpath:application.properties")
@ExtendWith(SpringExtension.class)
@Import(AppPropertiesReaderImpl.class)
class AppPropertiesReaderImplTest {

	@Autowired
	private AppPropertiesReaderImpl propReader;
	
	@Test
	void test_readDefinedProperty_success() {
		Integer length = propReader.get(VERIFICATION_TOKEN_LENGTH, Integer.class, 10);
		String email = propReader.get(VERIFICATION_EMAIL_ADDRESS, String.class, "");
		
		assertThat(length).isEqualTo(64);
		assertThat(email).isEqualTo("test@email.it");
	}
	
	@Test
	void test_whenPropertyTypeMismatch_shouldThrow() {
		assertThatThrownBy(() -> propReader.get(VERIFICATION_EMAIL_ADDRESS, Integer.class, 10))
			.isInstanceOf(AppUnableToReadPropertyException.class)
			.hasCauseInstanceOf(ConversionFailedException.class);
	}
	
	@Test
	void test_whenPropertyIsMissing_ShouldReturnDefaultValue() {
		Integer length = propReader.get(VERIFICATION_TOKEN_LENGTH+".test", Integer.class, 10);
		String email = propReader.get(VERIFICATION_EMAIL_ADDRESS+".test", String.class, "another@email.it");
		
		assertThat(length).isEqualTo(10);
		assertThat(email).isEqualTo("another@email.it");
	}

}
