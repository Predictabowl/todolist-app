package it.aldinucci.todoapp.application.service.util;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertTimeout;

import java.io.IOException;
import java.security.Security;
import java.time.Duration;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.AfterEach;
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

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { ApplicationBeansProvider.class, HtmlEmailSender.class })
@EnableConfigurationProperties
@PropertySource(value = "classpath:test.mail.properties")
class HtmlEmailSenderTest {

	@Autowired
	private HtmlEmailSender emailSender;
	
	private GreenMail mailServer;

	@BeforeEach
	void setUp() {
		Security.setProperty("ssl.SocketFactory.provider", DummySSLSocketFactory.class.getName());
		mailServer = new GreenMail(ServerSetupTest.SMTPS)
				.withConfiguration(GreenMailConfiguration.aConfig()
						.withUser("test@email.it", "testPassword"));
		mailServer.start();
	}
	
	@AfterEach
	void tearDown() {
		mailServer.stop();
	}

	@Test
	void test_sendEmail_success() throws MessagingException, IOException {
		String content = "<a href='#'> this is html </a>";
		
		assertThatCode(() -> 
				assertTimeout(Duration.ofSeconds(5), () -> 
					emailSender.send("unknown@email.org", "Sending Test", content)))
			.doesNotThrowAnyException();

		MimeMessage[] receivedMessages = mailServer.getReceivedMessages();
		assertThat(receivedMessages).hasSize(1);
		MimeMessage msg = receivedMessages[0];
		assertThat(msg.getSubject()).isEqualTo("Sending Test");
		assertThat(msg.getContent().toString())
			.contains(content);

		Address[] recipients = msg.getAllRecipients();
		assertThat(recipients).hasSize(1);
		assertThat(recipients[0].toString()).matches("unknown@email.org");

		Address[] from = msg.getFrom();
		assertThat(from).isNotNull();
		assertThat(from[0].toString()).matches("test@email.it");
	}
	
}
