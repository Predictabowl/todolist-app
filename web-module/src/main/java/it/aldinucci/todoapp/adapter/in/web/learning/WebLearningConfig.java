package it.aldinucci.todoapp.adapter.in.web.learning;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

//@Configuration
public class WebLearningConfig {

	@Bean
	public JavaMailSender getJavaMailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//	    mailSender.setHost("smtp.libero.it");
	    mailSender.setHost("localhost");
//	    mailSender.setPort(465);
	    mailSender.setPort(3025);
	    
//	    mailSender.setUsername("capitanfindus@inwind.it");
	    mailSender.setUsername("duke");
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
