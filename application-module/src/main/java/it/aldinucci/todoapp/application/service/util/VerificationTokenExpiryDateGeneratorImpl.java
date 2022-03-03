package it.aldinucci.todoapp.application.service.util;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.util.ApplicationPropertyNames;

@Component
public class VerificationTokenExpiryDateGeneratorImpl implements VerificationTokenExpiryDateGenerator {  
	
	public static final int DEFAULT_TOKEN_DURATION = 1440;
	
	private Environment env;
	
	@Autowired
	public VerificationTokenExpiryDateGeneratorImpl(Environment env) {
		this.env = env;
	}
	
	@Override
	public Date generate() {
		Integer property = env.getProperty(ApplicationPropertyNames.VERIFICATION_TOKEN_DURATION.get(),
				Integer.class, DEFAULT_TOKEN_DURATION);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)+property);
		return calendar.getTime();
	}
}
