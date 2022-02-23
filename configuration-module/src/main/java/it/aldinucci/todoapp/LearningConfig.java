package it.aldinucci.todoapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;

@Configuration
public class LearningConfig {
	
	@Autowired
	public LearningConfig(UserJPARepository userRepo) {
		super();
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		UserJPA user = new UserJPA("testUser@email.it", "username", passwordEncoder.encode("password")); 
		userRepo.save(user);
	}
	
}
