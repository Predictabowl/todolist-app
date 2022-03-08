package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.VerificationTokenJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.VerificationTokenJPARepository;
import it.aldinucci.todoapp.application.port.out.LoadVerificationTokenByEmailDriverPort;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class LoadVerificationTokenByEmailJPA implements LoadVerificationTokenByEmailDriverPort{
	
	private VerificationTokenJPARepository tokenRepo;
	private AppGenericMapper<VerificationTokenJPA, VerificationToken> mapper;
	
	
	@Autowired
	public LoadVerificationTokenByEmailJPA(VerificationTokenJPARepository tokenRepo,
			AppGenericMapper<VerificationTokenJPA, VerificationToken> mapper) {
		super();
		this.tokenRepo = tokenRepo;
		this.mapper = mapper;
	}



	@Override
	public Optional<VerificationToken> load(String email) {
		Optional<VerificationTokenJPA> tokenJPA = tokenRepo.findByUserEmail(email);
		if (tokenJPA.isEmpty())
			return Optional.empty();
		return Optional.of(mapper.map(tokenJPA.get()));
	}
}
