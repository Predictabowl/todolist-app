package it.aldinucci.todoapp.application.service.util;

import static it.aldinucci.todoapp.config.ApplicationPropertyNames.VERIFICATION_EMAIL_ADDRESS;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class HtmlEmailSender implements EmailSender {

	@Value("${" + VERIFICATION_EMAIL_ADDRESS + "}")
	private String emailAddress;

	private JavaMailSender emailSender;

	private final Logger logger;

	@Autowired
	public HtmlEmailSender(JavaMailSender emailSender) {
		super();
		this.emailSender = emailSender;
		logger = LoggerFactory.getLogger(HtmlEmailSender.class);
	}

	@Override
	public void send(String to, String subject, String content) {
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setSubject(subject);
			helper.setFrom(emailAddress);
			helper.setTo(to);
			helper.setText(content, true);
		} catch (MessagingException e) {
			logger.error("Error while creating email message", e);
		}

		emailSender.send(message);

	}

}
