package it.aldinucci.todoapp.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.SendResetPasswordEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.EmailLinkDTO;
import it.aldinucci.todoapp.application.service.util.EmailSender;

@Service
public class SendResetPasswordEmailService implements SendResetPasswordEmailUsePort {

	private final EmailSender emailSender;

	@Autowired
	public SendResetPasswordEmailService(EmailSender emailSender) {
		super();
		this.emailSender = emailSender;
	}

	/*
	 * Need to generalize the message with properties
	 */
	@Override
	public void send(EmailLinkDTO link) {
		emailSender.send(link.getEmail(), "Reset Password",
				String.format("Please click on the following link to reset your password:<br><br>"
						+ "<a href='%s'>Password Reset link</a>", link.getLink()));
	}


}
