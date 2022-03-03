package it.aldinucci.todoapp.application.service.util;

import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.domain.VerificationToken;

public interface CreateVerificationTokenService {

	public VerificationToken create(User user);
}
