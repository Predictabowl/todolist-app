package it.aldinucci.todoapp.application.service.util;

import java.util.Date;

public interface VerificationTokenExpiryDateGenerator {

	public Date generate();
}
