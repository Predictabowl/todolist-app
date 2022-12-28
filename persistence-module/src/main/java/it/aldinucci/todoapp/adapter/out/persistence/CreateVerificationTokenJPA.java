package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.VerificationTokenJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.repository.VerificationTokenJPARepository;
import it.aldinucci.todoapp.application.port.out.CreateUserVerificationTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.VerificationTokenData;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exception.AppUserAlreadyHaveVerificationTokenException;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class CreateVerificationTokenJPA implements CreateUserVerificationTokenDriverPort {

	private VerificationTokenJPARepository tokenRepo;
	private UserJPARepository userRepo;
	private AppGenericMapper<VerificationTokenJPA, VerificationToken> mapper;

	public CreateVerificationTokenJPA(VerificationTokenJPARepository tokenRepo, UserJPARepository userRepo,
			AppGenericMapper<VerificationTokenJPA, VerificationToken> mapper) {
		this.tokenRepo = tokenRepo;
		this.userRepo = userRepo;
		this.mapper = mapper;
	}


	@Override
	public VerificationToken create(VerificationTokenData token) throws AppUserNotFoundException, AppUserAlreadyHaveVerificationTokenException {
		UserJPA user = userRepo.findByEmail(token.userEmail()).orElseThrow(() ->
				new AppUserNotFoundException("User not found with email: "+token.userEmail()));
		
		Optional<VerificationTokenJPA> optional = tokenRepo.findByUser(user);
		if (optional.isPresent())
			throw new AppUserAlreadyHaveVerificationTokenException();
		
		VerificationTokenJPA tokenJpa = tokenRepo.save(new VerificationTokenJPA(user, token.expiryDate()));
		return mapper.map(tokenJpa);
	}

}
