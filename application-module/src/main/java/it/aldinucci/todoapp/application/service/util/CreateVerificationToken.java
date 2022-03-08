package it.aldinucci.todoapp.application.service.util;

import it.aldinucci.todoapp.domain.VerificationToken;

public interface CreateVerificationToken {

	public VerificationToken create(String email);
}
