package it.aldinucci.todoapp.application.service;


import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.hibernate.cfg.Environment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;

import it.aldinucci.todoapp.application.config.ApplicationBeansProvider;
import it.aldinucci.todoapp.application.port.in.dto.VerificantionLinkDTO;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationBeansProvider.class, SendVerificationEmailService.class})
@EnableConfigurationProperties
@PropertySource(value = "classpath:application.properties")
class SendVerificationEmailServiceTest {

	private static final String FIXTURE_VERIFICATION_URL = "http://localhost:8080/verification";

	@RegisterExtension
	static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
	  .withConfiguration(GreenMailConfiguration.aConfig().withUser("capitanfindus@email.it", "springboot"));
	
	@Autowired
	private SendVerificationEmailService sendEmail;
	
	
	@Test
	void test_sendEmail() throws MessagingException, IOException {
		VerificantionLinkDTO dto = new VerificantionLinkDTO(FIXTURE_VERIFICATION_URL, "tizio@email.it");
		
		sendEmail.send(dto);
		
		MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
		assertThat(receivedMessages).hasSize(1);
		MimeMessage msg = receivedMessages[0];
		Address[] recipients = msg.getAllRecipients();
		assertThat(recipients).hasSize(1);
		assertThat(recipients[0].toString()).matches("tizio@email.it");
		assertThat(msg.getSubject()).isEqualTo("Account Verification");
		assertThat(msg.getContent().toString()).contains(FIXTURE_VERIFICATION_URL);
	}

}
