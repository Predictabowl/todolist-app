package it.aldinucci.todoapp.application.handler;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.MimeMessageHelper;

import it.aldinucci.todoapp.exception.AppMessagingException;
import it.aldinucci.todoapp.exception.handler.ExceptionHandler;
import it.aldinucci.todoapp.exception.handler.ExceptionThrowingFunction;

public class MimeMessageHelperExceptionHandler
		implements ExceptionHandler<MimeMessageHelper, MimeMessage, MessagingException> {

	private MimeMessageHelper mimeMessageHelper;

	public MimeMessageHelperExceptionHandler(MimeMessageHelper mimeMessageHelper) {
		super();
		this.mimeMessageHelper = mimeMessageHelper;
	}

	@Override
	public MimeMessage doItWithHandler(
			ExceptionThrowingFunction<MimeMessageHelper, MimeMessage, MessagingException> code) {
		try {
			return code.apply(mimeMessageHelper);
		} catch (MessagingException e) {
			throw new AppMessagingException(e);
		}
	}

}
