package it.aldinucci.todoapp.application.service;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import it.aldinucci.todoapp.application.port.in.dto.EmailLinkDTO;
import it.aldinucci.todoapp.application.service.util.EmailSender;

class SendResetPasswordEmailServiceTest {

	private static final String FIXTURE_RESET_PASSWORD_URL = "http://localhost:8080/reset";

	private SendResetPasswordEmailService sendEmail;

	@Test
	void test_sendEmail(){
		EmailSender emailSender = mock(EmailSender.class);
		sendEmail = new SendResetPasswordEmailService(emailSender);
		
		EmailLinkDTO dto = new EmailLinkDTO(FIXTURE_RESET_PASSWORD_URL, "test@email.org");

		sendEmail.send(dto);

		verify(emailSender).send("test@email.org", 
				"Reset Password", 
				"Please click on the following link to reset your password:<br><br>"
				+ "<a href='"+FIXTURE_RESET_PASSWORD_URL+"'>Password Reset link</a>");
	}
}
