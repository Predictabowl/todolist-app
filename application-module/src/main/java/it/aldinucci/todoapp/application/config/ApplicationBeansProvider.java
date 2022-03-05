package it.aldinucci.todoapp.application.config;

import static it.aldinucci.todoapp.config.ApplicationPropertyNames.*;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

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

	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", emailProtocol);
	    props.put("mail.smtp.auth", emailAuth);
	    props.put("mail.smtp.ssl.enable", emailSsl);
	    props.put("mail.debug", emailDebug);
	    
	    return mailSender;
	}
}
