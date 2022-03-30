package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ResetPasswordTokenJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ResetPasswordTokenJPARepository;
import it.aldinucci.todoapp.application.port.out.LoadResetPasswordTokenDriverPort;
import it.aldinucci.todoapp.domain.ResetPasswordToken;
import it.aldinucci.todoapp.mapper.AppGenericMapper;
import it.aldinucci.todoapp.util.ValidationUtils;

@Component
public class LoadResetPasswordTokenJPA implements LoadResetPasswordTokenDriverPort{

	private ResetPasswordTokenJPARepository tokenRepo;
	private AppGenericMapper<ResetPasswordTokenJPA, ResetPasswordToken> mapper;
	
	@Autowired
	public LoadResetPasswordTokenJPA(ResetPasswordTokenJPARepository tokenRepo,
			AppGenericMapper<ResetPasswordTokenJPA, ResetPasswordToken> mapper) {
		super();
		this.tokenRepo = tokenRepo;
		this.mapper = mapper;
	}


	@Override
	public Optional<ResetPasswordToken> load(String tokenString) {
		if (!ValidationUtils.isValidUUID(tokenString))
			return Optional.empty();
			
		Optional<ResetPasswordTokenJPA> token = tokenRepo.findByToken(UUID.fromString(tokenString));
		if(token.isEmpty())
			return Optional.empty();
		
		return Optional.of(mapper.map(token.get()));
	}

}
