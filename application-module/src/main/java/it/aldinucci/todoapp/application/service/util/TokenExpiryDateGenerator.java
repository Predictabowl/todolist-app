package it.aldinucci.todoapp.application.service.util;

import java.time.Duration;
import java.util.Date;

public interface TokenExpiryDateGenerator {

	public Date generate(String durationPropertyName, int defaultMinutes);
}
