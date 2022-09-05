package it.aldinucci.todoapp.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.SendVerificationEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.EmailLinkDTO;
import it.aldinucci.todoapp.application.service.util.EmailSender;

@Service
public class SendVerificationEmailService implements SendVerificationEmailUsePort {

	private final EmailSender emailSender;

	@Autowired
	public SendVerificationEmailService(EmailSender emailSender) {
		super();
		this.emailSender = emailSender;
	}
	
	/*
	 * Need to generalize the message with properties
	 */
	@Override
	public void send(EmailLinkDTO link) {
		emailSender.send(link.getEmail(), "Account Verification",
				String.format("Please click on the following link to activate your account:<br><br>"
						+ "<a href='%s'>Verification link</a>", link.getLink()));
	}

}
