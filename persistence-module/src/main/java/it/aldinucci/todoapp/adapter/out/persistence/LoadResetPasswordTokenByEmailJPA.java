package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ResetPasswordTokenJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ResetPasswordTokenJPARepository;
import it.aldinucci.todoapp.application.port.out.LoadResetPasswordTokenByEmailDriverPort;
import it.aldinucci.todoapp.domain.ResetPasswordToken;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class LoadResetPasswordTokenByEmailJPA implements LoadResetPasswordTokenByEmailDriverPort{

	private ResetPasswordTokenJPARepository tokenRepo;
	private AppGenericMapper<ResetPasswordTokenJPA, ResetPasswordToken> mapper;

	public LoadResetPasswordTokenByEmailJPA(ResetPasswordTokenJPARepository tokenRepo,
			AppGenericMapper<ResetPasswordTokenJPA, ResetPasswordToken> mapper) {
		super();
		this.tokenRepo = tokenRepo;
		this.mapper = mapper;
	}


	@Override
	public Optional<ResetPasswordToken> load(String email) {
		Optional<ResetPasswordTokenJPA> optional = tokenRepo.findByUserEmail(email);
		if (optional.isEmpty())
			return Optional.empty();
		
		return Optional.of(mapper.map(optional.get()));
	}

}
