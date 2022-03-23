package it.aldinucci.todoapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import it.aldinucci.todoapp.util.AppPasswordEncoder;
import it.aldinucci.todoapp.util.AppPasswordEncoderImpl;

@Configuration
public class CommonsBeansProvider {

	@Bean 
	public AppPasswordEncoder getAppPasswordEncoder(){
		return new AppPasswordEncoderImpl(new BCryptPasswordEncoder());
	}
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return getAppPasswordEncoder().getEncoder();
	}

}
