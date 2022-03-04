package it.aldinucci.todoapp.util;

public interface AppPropertiesReader {

	public <T> T get(String propertyName,Class<T> targetType, T defaultValue);
}
