package it.aldinucci.todoapp.util;

import org.springframework.security.crypto.password.PasswordEncoder;

public interface AppPasswordEncoder {

	public String encode(String rawPassword);
	
	public PasswordEncoder getEncoder();
}
