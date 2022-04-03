package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.VerificationTokenJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.VerificationTokenJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.util.ValidateId;
import it.aldinucci.todoapp.application.port.out.LoadVerificationTokenDriverPort;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class LoadVerificationTokenJPA implements LoadVerificationTokenDriverPort{

	private VerificationTokenJPARepository tokenRepo;
	private AppGenericMapper<VerificationTokenJPA, VerificationToken> mapper;
	private ValidateId<UUID> validator;
	

	@Autowired
	public LoadVerificationTokenJPA(VerificationTokenJPARepository tokenRepo,
			AppGenericMapper<VerificationTokenJPA, VerificationToken> mapper, ValidateId<UUID> validator) {
		super();
		this.tokenRepo = tokenRepo;
		this.mapper = mapper;
		this.validator = validator;
	}



	@Override
	public Optional<VerificationToken> load(String tokenString) {
		if (!validator.isValid(tokenString))
			return Optional.empty();
		
		Optional<VerificationTokenJPA> tokenJPA = tokenRepo.findByToken(validator.getId());
		if (tokenJPA.isEmpty())
			return Optional.empty();
		return Optional.of(mapper.map(tokenJPA.get()));
	}

}
