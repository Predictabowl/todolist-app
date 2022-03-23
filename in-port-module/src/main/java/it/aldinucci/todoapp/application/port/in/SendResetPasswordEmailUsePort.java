package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.EmailLinkDTO;

public interface SendResetPasswordEmailUsePort {

	public void send(EmailLinkDTO link);
}
