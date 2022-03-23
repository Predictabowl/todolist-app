package it.aldinucci.todoapp.application.service.util;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.util.AppPropertiesReader;

@Component
public class TokenExpiryDateGeneratorImpl implements TokenExpiryDateGenerator {  
	
	private AppPropertiesReader propReader;
	
	@Autowired
	public TokenExpiryDateGeneratorImpl(AppPropertiesReader propReader) {
		this.propReader = propReader;
	}


	@Override
	public Date generate(String durationPropertyName, int defaultMinutes) {
		Integer property = propReader.get(durationPropertyName,
				Integer.class, defaultMinutes);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, property);
		return calendar.getTime();
	}
}
