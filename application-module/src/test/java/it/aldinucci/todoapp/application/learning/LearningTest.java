package it.aldinucci.todoapp.application.learning;

import static org.assertj.core.api.Assertions.assertThat;

import it.aldinucci.todoapp.application.config.ApplicationBeansProvider;
import it.aldinucci.todoapp.util.ApplicationPropertyNames;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@EnableConfigurationProperties
@PropertySource(value = "classpath:application.properties")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationBeansProvider.class})
class LearningTest {

	@Autowired
	private Environment env;
	
	@Test
	void test_readEnvProperty() {
		String property = env.getProperty(ApplicationPropertyNames.VERIFICATION_EMAIL_ADDRESS);

		assertThat(property).matches("test@email.it");
	}
}
