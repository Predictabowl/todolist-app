package it.aldinucci.todoapp.application.config;

import static it.aldinucci.todoapp.config.ApplicationPropertyNames.VERIFICATION_EMAIL_ADDRESS;
import static it.aldinucci.todoapp.config.ApplicationPropertyNames.VERIFICATION_EMAIL_AUTH;
import static it.aldinucci.todoapp.config.ApplicationPropertyNames.VERIFICATION_EMAIL_DEBUG;
import static it.aldinucci.todoapp.config.ApplicationPropertyNames.VERIFICATION_EMAIL_HOST;
import static it.aldinucci.todoapp.config.ApplicationPropertyNames.VERIFICATION_EMAIL_PASSWORD;
import static it.aldinucci.todoapp.config.ApplicationPropertyNames.VERIFICATION_EMAIL_PORT;
import static it.aldinucci.todoapp.config.ApplicationPropertyNames.VERIFICATION_EMAIL_PROTOCOL;
import static it.aldinucci.todoapp.config.ApplicationPropertyNames.VERIFICATION_EMAIL_SSL;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import it.aldinucci.todoapp.application.handler.MimeMessageHelperExceptionHandler;
import it.aldinucci.todoapp.exception.handler.ExceptionHandler;

@Configuration
public class ApplicationBeansProvider {

	@Value("${"+VERIFICATION_EMAIL_ADDRESS+"}")
	private String emailAddress;
	
	@Value("${"+VERIFICATION_EMAIL_HOST+"}")
	private String emailHost;
	
	@Value("${"+VERIFICATION_EMAIL_PORT+":465}")
	private int emailPort;
	
	@Value("${"+VERIFICATION_EMAIL_PASSWORD+"}")
	private String emailPassword;
	
	@Value("${"+VERIFICATION_EMAIL_PROTOCOL+":smtp}")
	private String emailProtocol;
	
	@Value("${"+VERIFICATION_EMAIL_AUTH+":true}")
	private String emailAuth;
	
	@Value("${"+VERIFICATION_EMAIL_SSL+":true}")
	private String emailSsl;
	
	@Value("${"+VERIFICATION_EMAIL_DEBUG+":false}")
	private String emailDebug;
	
	@Bean
	public JavaMailSender getJavaMailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost(emailHost);
	    mailSender.setPort(emailPort);
	    mailSender.setUsername(emailAddress);
	    mailSender.setPassword(emailPassword);
	    mailSender.setProtocol(emailProtocol);

	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.smtp.auth", emailAuth);
	    props.put("mail.smtp.ssl.enable", emailSsl);
	    props.put("mail.debug", emailDebug);
	    
	    return mailSender;
	}
	
	@Bean
	public MimeMessage getMimeMessage() {
		return getJavaMailSender().createMimeMessage();
	}
	
	@Bean
	public MimeMessageHelper getMimeMessageHelper() {
		return new MimeMessageHelper(getMimeMessage());
	}
	
	@Bean
	public ExceptionHandler<MimeMessageHelper, MimeMessage, MessagingException> getMimeExceptionHandler(){
		return new MimeMessageHelperExceptionHandler(getMimeMessageHelper());
	}
}
