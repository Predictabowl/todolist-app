package it.aldinucci.todoapp.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.SendVerificationEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.VerificantionLinkDTO;

@Service
public class SendVerificationEmailService implements SendVerificationEmailUsePort{ 
	
	private JavaMailSender emailSender;
	
	@Autowired
	public SendVerificationEmailService(JavaMailSender emailSender) {
		this.emailSender = emailSender;
	}

	@Override
	public void send(VerificantionLinkDTO link) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("capitanfindus@inwind.it");
		message.setTo(link.getEmail());
		message.setSubject("Account Verification");
		message.setText("Verification email. link: "+link.getLink());
		emailSender.send(message);
		
	}

}
