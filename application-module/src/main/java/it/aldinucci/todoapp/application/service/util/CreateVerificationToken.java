package it.aldinucci.todoapp.application.service.util;

import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exception.AppVerificationTokenAlreadyExistsException;

public interface CreateVerificationToken {

	public VerificationToken create(String email) throws AppVerificationTokenAlreadyExistsException;
}
