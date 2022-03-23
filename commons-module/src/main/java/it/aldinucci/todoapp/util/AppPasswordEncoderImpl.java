package it.aldinucci.todoapp.util;

import org.springframework.security.crypto.password.PasswordEncoder;

public class AppPasswordEncoderImpl implements AppPasswordEncoder {

	private PasswordEncoder encoder;
	
	public AppPasswordEncoderImpl(PasswordEncoder encoder) {
		super();
		this.encoder = encoder;
	}

	@Override
	public String encode(String rawPassword) {
		return encoder.encode(rawPassword);
	}

	@Override
	public PasswordEncoder getEncoder() {
		return encoder;
	}

}
