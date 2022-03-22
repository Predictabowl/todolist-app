package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ResetPasswordTokenJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ResetPasswordTokenJPARepository;
import it.aldinucci.todoapp.application.port.out.LoadResetPasswordTokenDriverPort;
import it.aldinucci.todoapp.domain.ResetPasswordToken;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

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
		Optional<ResetPasswordTokenJPA> token = tokenRepo.findByToken(tokenString);
		if(token.isEmpty())
			return Optional.empty();
		
		return Optional.of(mapper.map(token.get()));
	}

}