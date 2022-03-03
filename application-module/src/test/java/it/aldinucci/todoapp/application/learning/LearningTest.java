package it.aldinucci.todoapp.application.learning;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.application.util.ApplicationPropertyNames;

@EnableConfigurationProperties
@PropertySource(value = "classpath:application.properties")
@ExtendWith(SpringExtension.class)
class LearningTest {

	@Autowired
	private Environment env;
	
	@Test
	void test_readEnvProperty() {
		String property = env.getProperty(ApplicationPropertyNames.VERIFICATION_SENDER_EMAIL);

		assertThat(property).matches("capitanfindus@email.it");
	}
}
