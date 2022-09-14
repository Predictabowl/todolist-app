package it.aldinucci.todoapp.application.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import javax.mail.MessagingException;

import org.junit.jupiter.api.Test;

import it.aldinucci.todoapp.application.port.in.dto.EmailLinkDTO;
import it.aldinucci.todoapp.application.service.util.EmailSender;

class SendVerificationEmailServiceTest {

	private static final String FIXTURE_VERIFICATION_URL = "http://localhost:8080/verification";

	private EmailSender emailSender;
	
	private SendVerificationEmailService sendEmail;

	@Test
	void test_sendEmail() throws MessagingException, IOException {
		emailSender = mock(EmailSender.class);
		sendEmail = new SendVerificationEmailService(emailSender);
		
		EmailLinkDTO dto = new EmailLinkDTO(FIXTURE_VERIFICATION_URL, "test@email.org");

		sendEmail.send(dto);

		verify(emailSender).send("test@email.org", 
				"Account Verification", 
				"Please click on the following link to activate your account:<br><br>"
				+ "<a href='"+FIXTURE_VERIFICATION_URL+"'>Verification link</a>");
	}

}
