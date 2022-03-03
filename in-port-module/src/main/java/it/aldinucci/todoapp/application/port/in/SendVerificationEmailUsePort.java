package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.VerificantionLinkDTO;

public interface SendVerificationEmailUsePort {

	public void send(VerificantionLinkDTO link);
}
