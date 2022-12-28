package it.aldinucci.todoapp.adapter.out.persistence;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;
import it.aldinucci.todoapp.application.port.out.UserExistsDriverPort;

@Component
public class UserExistsJPA implements UserExistsDriverPort{

	private UserJPARepository userRepo;
	
	
	public UserExistsJPA(UserJPARepository userRepo) {
		super();
		this.userRepo = userRepo;
	}

	@Override
	public boolean exists(String email) {
		return userRepo.existsByEmail(email);
	}

}
