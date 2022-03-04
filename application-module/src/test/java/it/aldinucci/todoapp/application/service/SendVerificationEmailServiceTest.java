package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.security.Security;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.util.DummySSLSocketFactory;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

import it.aldinucci.todoapp.application.config.ApplicationBeansProvider;
import it.aldinucci.todoapp.application.port.in.dto.VerificantionLinkDTO;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { ApplicationBeansProvider.class, SendVerificationEmailService.class })
@EnableConfigurationProperties
@PropertySource(value = "classpath:test.mail.properties")
class SendVerificationEmailServiceTest {

	private static final String FIXTURE_VERIFICATION_URL = "http://localhost:8080/verification";

	@Autowired
	private SendVerificationEmailService sendEmail;

	private GreenMail mailServer;

	@BeforeEach
	void setUp() {
		Security.setProperty("ssl.SocketFactory.provider", DummySSLSocketFactory.class.getName());
		mailServer = new GreenMail(ServerSetupTest.SMTPS)
				.withConfiguration(GreenMailConfiguration.aConfig()
						.withUser("test@email.it", "testPassword"));
		mailServer.start();
	}

	@Test
	void test_sendEmail() throws MessagingException, IOException {
		VerificantionLinkDTO dto = new VerificantionLinkDTO(FIXTURE_VERIFICATION_URL, "unknown@email.org");

		sendEmail.send(dto);

		MimeMessage[] receivedMessages = mailServer.getReceivedMessages();
		assertThat(receivedMessages).hasSize(1);
		MimeMessage msg = receivedMessages[0];
		assertThat(msg.getSubject()).isEqualTo("Account Verification");
		assertThat(msg.getContent().toString()).contains(FIXTURE_VERIFICATION_URL);

		Address[] recipients = msg.getAllRecipients();
		assertThat(recipients).hasSize(1);
		assertThat(recipients[0].toString()).matches("unknown@email.org");

		Address[] from = msg.getFrom();
		assertThat(from).hasSize(1);
		assertThat(from[0].toString()).matches("noreply@email.it");
	}

}
