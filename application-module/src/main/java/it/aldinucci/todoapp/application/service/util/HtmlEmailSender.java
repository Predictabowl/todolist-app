package it.aldinucci.todoapp.application.service.util;

import static it.aldinucci.todoapp.config.ApplicationPropertyNames.VERIFICATION_EMAIL_ADDRESS;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.exception.handler.ExceptionHandler;

@Component
public class HtmlEmailSender implements EmailSender {

	@Value("${" + VERIFICATION_EMAIL_ADDRESS + "}")
	private String emailAddress;

	private JavaMailSender emailSender;
	private ExceptionHandler<MimeMessageHelper, MimeMessage, MessagingException> helperHandler;

	@Autowired
	public HtmlEmailSender(JavaMailSender emailSender,
			ExceptionHandler<MimeMessageHelper, MimeMessage, MessagingException> helperHandler) {
		super();
		this.emailSender = emailSender;
		this.helperHandler = helperHandler;
	}

	@Override
	public void send(String to, String subject, String content) {
		MimeMessage mimeMessage = helperHandler.doItWithHandler(t -> {
			t.setSubject(subject);
			t.setFrom(emailAddress);
			t.setTo(to);
			t.setText(content, true);
			return t.getMimeMessage();
		});
		emailSender.send(mimeMessage);
	}

}
