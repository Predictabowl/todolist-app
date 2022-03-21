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

class SendResetPasswordEmailServiceTest {

	private static final String FIXTURE_RESET_PASSWORD_URL = "http://localhost:8080/reset";

	@Mock
	private EmailSender emailSender;
	
	private SendResetPasswordEmailService sendEmail;

	@BeforeEach
	void setUp() {
		openMocks(this);
		sendEmail = new SendResetPasswordEmailService(emailSender); 
	}

	@Test
	void test_sendEmail() throws MessagingException, IOException {
		EmailLinkDTO dto = new EmailLinkDTO(FIXTURE_RESET_PASSWORD_URL, "test@email.org");

		sendEmail.send(dto);

		verify(emailSender).send("test@email.org", 
				"Reset Password", 
				"Please click on the following link to reset your password:<br><br>"
				+ "<a href='"+FIXTURE_RESET_PASSWORD_URL+"'>Password Reset link</a>");
	}
}
