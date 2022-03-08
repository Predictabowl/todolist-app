package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.VerificationLinkDTO;

public interface SendVerificationEmailUsePort {

	public void send(VerificationLinkDTO link);
}
