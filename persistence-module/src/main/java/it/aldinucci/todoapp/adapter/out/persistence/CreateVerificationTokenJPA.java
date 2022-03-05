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
import it.aldinucci.todoapp.exceptions.AppUserAlreadyHaveVerificationTokenException;
import it.aldinucci.todoapp.exceptions.AppUserNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class CreateVerificationTokenJPA implements CreateVerificationTokenDriverPort {

	private VerificationTokenJPARepository tokenRepo;
	private UserJPARepository userRepo;
	private AppGenericMapper<VerificationTokenJPA, VerificationToken> mapper;

	@Autowired
	public CreateVerificationTokenJPA(VerificationTokenJPARepository tokenRepo, UserJPARepository userRepo,
			AppGenericMapper<VerificationTokenJPA, VerificationToken> mapper) {
		this.tokenRepo = tokenRepo;
		this.userRepo = userRepo;
		this.mapper = mapper;
	}


	@Override
	public VerificationToken create(VerificationTokenDTOOut token) throws AppUserNotFoundException, AppUserAlreadyHaveVerificationTokenException {
		UserJPA user = userRepo.findByEmail(token.getUserEmail()).orElseThrow(() ->
				new AppUserNotFoundException("User not found with email: "+token.getUserEmail()));
		
		Optional<VerificationTokenJPA> optional = tokenRepo.findByUser(user);
		if (optional.isPresent())
			throw new AppUserAlreadyHaveVerificationTokenException();
		
		VerificationTokenJPA tokenJpa = tokenRepo.save(new VerificationTokenJPA(token.getToken(), user, token.getExpiryDate()));
		return mapper.map(tokenJpa);
	}

}