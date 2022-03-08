package it.aldinucci.todoapp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.exceptions.AppUnableToReadPropertyException;

@Component
public class AppPropertiesReaderImpl implements AppPropertiesReader {

	private Environment env;

	@Autowired
	public AppPropertiesReaderImpl(Environment env) {
		this.env = env;
	}

	@Override
	public <T> T get(String propertyName, Class<T> targetType, T defaultValue) {
		T value;
		try {
			value = env.getProperty(propertyName, targetType, defaultValue);
		} catch (ConversionFailedException e) {
			throw new AppUnableToReadPropertyException(e);
		}
		return value;
	}

}
