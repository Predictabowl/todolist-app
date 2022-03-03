package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.VerificationTokenJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.repository.VerificationTokenJPARepository;
import it.aldinucci.todoapp.application.port.out.CreateVerificationTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.VerificationTokenDTOOut;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exceptions.AppUserNotFoundException;

@Component
public class CreateVerificationTokenJPA implements CreateVerificationTokenDriverPort {

	private VerificationTokenJPARepository tokenRepo;
	private UserJPARepository userRepo;

	@Autowired
	public CreateVerificationTokenJPA(VerificationTokenJPARepository tokenRepo, UserJPARepository userRepo) {
		this.tokenRepo = tokenRepo;
		this.userRepo = userRepo;
	}

	@Override
	public VerificationToken create(VerificationTokenDTOOut token) throws AppUserNotFoundException {
//		Optional<UserJPA> user = userRepo.findByEmail(token.getUserEmail());
//		VerificationTokenJPA tokenJpa = new VerificationTokenJPA(token.getToken(), user.get(), token.getExpiryDate());
//		tokenRepo.save(tokenJpa);
		throw new UnsupportedOperationException("Method not implemented");
	}

}
