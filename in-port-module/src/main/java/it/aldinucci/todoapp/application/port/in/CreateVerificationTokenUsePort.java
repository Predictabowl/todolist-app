package it.aldinucci.todoapp.application.port.in;

import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.domain.VerificationToken;

public interface CreateVerificationTokenUsePort {

	public VerificationToken create(User user);
}
