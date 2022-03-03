package it.aldinucci.todoapp.application.port.out;

import java.util.Optional;

import it.aldinucci.todoapp.domain.VerificationToken;

public interface LoadVerificationTokenDriverPort {

	public Optional<VerificationToken> load(String token);
}
