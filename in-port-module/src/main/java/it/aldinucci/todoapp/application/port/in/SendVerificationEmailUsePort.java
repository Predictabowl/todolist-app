package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.application.port.in.dto.UrlLinkDTO;

public interface SendVerificationEmailUsePort {

	public void send(UrlLinkDTO link);
}
