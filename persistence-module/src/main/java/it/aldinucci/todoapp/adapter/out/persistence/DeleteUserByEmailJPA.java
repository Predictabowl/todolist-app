package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.VerificationTokenJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.repository.VerificationTokenJPARepository;
import it.aldinucci.todoapp.application.port.out.DeleteUserByEmailDriverPort;

@Component
public class DeleteUserByEmailJPA implements DeleteUserByEmailDriverPort {

	private UserJPARepository userRepo;
	private VerificationTokenJPARepository tokenRepo;

	@Autowired
	public DeleteUserByEmailJPA(UserJPARepository userRepo, VerificationTokenJPARepository tokenRepo) {
		this.userRepo = userRepo;
		this.tokenRepo = tokenRepo;
	}

	@Override
	public void delete(String email) {
		Optional<VerificationTokenJPA> token = tokenRepo.findByUserEmail(email);
		if (token.isPresent())
			tokenRepo.delete(token.get());

		Optional<UserJPA> user = userRepo.findByEmail(email);
		if (user.isPresent())
			userRepo.delete(user.get());
	}

}
