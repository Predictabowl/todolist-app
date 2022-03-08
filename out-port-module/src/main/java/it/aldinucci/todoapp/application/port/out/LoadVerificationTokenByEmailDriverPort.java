package it.aldinucci.todoapp.application.port.out;

import java.util.Optional;

import it.aldinucci.todoapp.domain.VerificationToken;

public interface LoadVerificationTokenByEmailDriverPort {

	public Optional<VerificationToken> load(String email);
}
