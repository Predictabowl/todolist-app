package it.aldinucci.todoapp.application.service.util;

import static it.aldinucci.todoapp.config.ApplicationPropertyNames.VERIFICATION_EMAIL_ADDRESS;

import javax.mail.MessagingException;

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
	private MimeMessageHelper mimeMessageHelper;

	@Autowired
	public HtmlEmailSender(JavaMailSender emailSender, MimeMessageHelper mimeMessageHelper) {
		super();
		this.emailSender = emailSender;
		this.mimeMessageHelper = mimeMessageHelper;
	}

	@Override
	public void send(String to, String subject, String content) {
		try {
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setFrom(emailAddress);
			mimeMessageHelper.setTo(to);
			mimeMessageHelper.setText(content, true);
			emailSender.send(mimeMessageHelper.getMimeMessage());
		} catch (MessagingException e) {
			LoggerFactory.getLogger(HtmlEmailSender.class)
				.error("Error while creating email message", e);
		}
	}

}
