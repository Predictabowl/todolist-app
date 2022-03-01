package it.aldinucci.todoapp.adapter.in.web.learning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl {

	private JavaMailSender emailSender;

	@Autowired
	public EmailServiceImpl(JavaMailSender emailSender) {
		this.emailSender = emailSender;
	}
	
	public void sendSimpleMessage() {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("capitanfindus@inwind.it");
		message.setTo("piero.aldinucci@libero.it");
		message.setSubject("Andiamo al mare");
		message.setText("Ti piacerebbe eh?");
		emailSender.send(message);
	}
}
