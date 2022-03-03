package it.aldinucci.todoapp.application.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import it.aldinucci.todoapp.application.util.ApplicationPropertyNames;

@Configuration
public class ApplicationBeansProvider {


	@Value("${"+ApplicationPropertyNames.VERIFICATION_SENDER_EMAIL+"}")
	private String senderEmail;
	
	@Bean
	public JavaMailSender getJavaMailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//	    mailSender.setHost("smtp.libero.it");
	    mailSender.setHost("localhost");
//	    mailSender.setPort(465);
	    mailSender.setPort(3025);
	    
//	    mailSender.setUsername("capitanfindus@inwind.it");
//	    mailSender.setUsername("luke");
	    mailSender.setUsername(senderEmail);
	    mailSender.setPassword("springboot");
	    
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
//	    props.put("mail.smtp.starttls.enable", "true");
//	    props.put("mail.smtp.starttls.required", "true");
	    props.put("mail.smtp.ssl.checkserveridentity", "true");
	    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	    props.put("mail.debug", "true");
	    
	    return mailSender;
	}
}
