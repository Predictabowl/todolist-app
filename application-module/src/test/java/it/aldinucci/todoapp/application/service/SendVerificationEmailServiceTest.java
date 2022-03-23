package it.aldinucci.todoapp.application.service;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

import java.io.IOException;

import javax.mail.MessagingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import it.aldinucci.todoapp.application.port.in.dto.EmailLinkDTO;
import it.aldinucci.todoapp.application.service.util.EmailSender;

class SendVerificationEmailServiceTest {

	private static final String FIXTURE_VERIFICATION_URL = "http://localhost:8080/verification";

	@Mock
	private EmailSender emailSender;
	
	private SendVerificationEmailService sendEmail;

	@BeforeEach
	void setUp() {
		openMocks(this);
		sendEmail = new SendVerificationEmailService(emailSender); 
	}

	@Test
	void test_sendEmail() throws MessagingException, IOException {
		EmailLinkDTO dto = new EmailLinkDTO(FIXTURE_VERIFICATION_URL, "test@email.org");

		sendEmail.send(dto);

		verify(emailSender).send("test@email.org", 
				"Account Verification", 
				"Please click on the following link to activate your account:<br><br>"
				+ "<a href='"+FIXTURE_VERIFICATION_URL+"'>Verification link</a>");
	}

}
