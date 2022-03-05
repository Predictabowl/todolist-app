package it.aldinucci.todoapp.application.service;

import static it.aldinucci.todoapp.config.ApplicationPropertyNames.VERIFICATION_EMAIL_ADDRESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.SendVerificationEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.VerificantionLinkDTO;

@Service
public class SendVerificationEmailService implements SendVerificationEmailUsePort {

	@Value("${" + VERIFICATION_EMAIL_ADDRESS + "}")
	private String emailAddress;

	private JavaMailSender emailSender;

	@Autowired
	public SendVerificationEmailService(JavaMailSender emailSender) {
		this.emailSender = emailSender;
	}

	@Override
	public void send(VerificantionLinkDTO link) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setSubject("Account Verification");
		message.setFrom(emailAddress);
		message.setTo(link.getEmail());
		message.setText("Verification email...\n" +
				"Click on the following link: " + link.getLink());
		emailSender.send(message);

	}

}
