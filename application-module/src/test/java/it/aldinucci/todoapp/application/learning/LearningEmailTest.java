package it.aldinucci.todoapp.application.learning;

import java.io.IOException;

import javax.mail.MessagingException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.application.config.ApplicationBeansProvider;
import it.aldinucci.todoapp.application.port.in.dto.VerificantionLinkDTO;
import it.aldinucci.todoapp.application.service.SendVerificationEmailService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { ApplicationBeansProvider.class, SendVerificationEmailService.class })
@EnableConfigurationProperties
@PropertySource(value = "classpath:test.mail2.properties")
class LearningEmailTest {

	private static final String FIXTURE_VERIFICATION_URL = "http://localhost:8080/verification";

	@Autowired
	private SendVerificationEmailService sendEmail;


	@Test
	void test_sendEmail() throws MessagingException, IOException {
		VerificantionLinkDTO dto = new VerificantionLinkDTO(FIXTURE_VERIFICATION_URL, "piero.aldinucci@libero.it");

		sendEmail.send(dto);

	}

}
