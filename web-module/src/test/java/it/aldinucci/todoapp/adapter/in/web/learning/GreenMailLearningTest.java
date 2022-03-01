package it.aldinucci.todoapp.adapter.in.web.learning;

import static org.assertj.core.api.Assertions.assertThat;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;

@SpringBootTest(classes = {EmailServiceImpl.class})
@ContextConfiguration(classes = {WebLearningConfig.class})
@ExtendWith({SpringExtension.class})
class GreenMailLearningTest {
	
//	@RegisterExtension
//	static GreenMailExtension greenMail = new GreenMailExtension(ServerSetup.SMTP); 
	@RegisterExtension
	static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
	  .withConfiguration(GreenMailConfiguration.aConfig().withUser("duke", "springboot"));
//	  .withPerMethodLifecycle(false);
	
	@Autowired
	private EmailServiceImpl service;
	
	@Test
	void test_learning() throws MessagingException {
		service.sendSimpleMessage();
		MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
		assertThat(receivedMessages).hasSize(1);
		MimeMessage msg = receivedMessages[0];
		assertThat(msg.getSubject()).isEqualTo("Andiamo al mare");
	}
	
//	@Test
//	void test_greenMail() throws AddressException, MessagingException {
//		GreenMail greenMail = new GreenMail();
////		GreenMail greenMail = greenMailBean.getGreenMail();
//		Session smtpSession = greenMail.getSmtp().createSession();
//		
//		Message msg = new MimeMessage(smtpSession);
//	    msg.setFrom(new InternetAddress("capitanfindus@inwind.it"));
//	    msg.addRecipient(Message.RecipientType.TO,
//	            new InternetAddress("piero.aldinucci@libero.it"));
//	    msg.setSubject("Email sent to GreenMail via plain JavaMail");
//	    msg.setText("Fetch me via IMAP");
//	    Transport.send(msg);
////	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
////	    mailSender.send(msg);
//		greenMail.stop();
//	}

}
