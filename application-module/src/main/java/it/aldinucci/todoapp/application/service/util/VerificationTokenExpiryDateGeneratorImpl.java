package it.aldinucci.todoapp.application.service.util;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.util.AppPropertiesReader;
import it.aldinucci.todoapp.util.ApplicationPropertyNames;

@Component
public class VerificationTokenExpiryDateGeneratorImpl implements VerificationTokenExpiryDateGenerator {  
	
	public static final int DEFAULT_TOKEN_DURATION = 1440;
	
	private AppPropertiesReader propReader;
	
	@Autowired
	public VerificationTokenExpiryDateGeneratorImpl(AppPropertiesReader propReader) {
		this.propReader = propReader;
	}


	@Override
	public Date generate() {
		Integer property = propReader.get(ApplicationPropertyNames.VERIFICATION_TOKEN_DURATION,
				Integer.class, DEFAULT_TOKEN_DURATION);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, property);
		return calendar.getTime();
	}
}
