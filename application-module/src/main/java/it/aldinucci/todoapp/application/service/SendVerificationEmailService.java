package it.aldinucci.todoapp.application.service;

import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.SendVerificationEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UrlLinkDTO;

@Service
public class SendVerificationEmailService implements SendVerificationEmailUsePort{ 
	
	@Override
	public void send(UrlLinkDTO link) {
		// TODO Auto-generated method stub
		
	}

}
