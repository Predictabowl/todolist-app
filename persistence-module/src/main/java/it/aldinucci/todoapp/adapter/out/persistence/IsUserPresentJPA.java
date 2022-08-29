package it.aldinucci.todoapp.adapter.out.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;
import it.aldinucci.todoapp.application.port.out.IsUserPresentDriverPort;

@Component
public class IsUserPresentJPA implements IsUserPresentDriverPort{

	private UserJPARepository userRepo;
	
	@Autowired
	public IsUserPresentJPA(UserJPARepository userRepo) {
		this.userRepo = userRepo;
	}


	@Override
	public boolean isPresent(String email) {
		return userRepo.findByEmail(email).isPresent(); 
	}

}
