package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ResetPasswordTokenJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ResetPasswordTokenJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.util.ValidateId;
import it.aldinucci.todoapp.application.port.out.LoadResetPasswordTokenDriverPort;
import it.aldinucci.todoapp.domain.ResetPasswordToken;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class LoadResetPasswordTokenJPA implements LoadResetPasswordTokenDriverPort {

	private ResetPasswordTokenJPARepository tokenRepo;
	private AppGenericMapper<ResetPasswordTokenJPA, ResetPasswordToken> mapper;
	private ValidateId<UUID> validator;

	public LoadResetPasswordTokenJPA(ResetPasswordTokenJPARepository tokenRepo,
			AppGenericMapper<ResetPasswordTokenJPA, ResetPasswordToken> mapper, ValidateId<UUID> validator) {
		super();
		this.tokenRepo = tokenRepo;
		this.mapper = mapper;
		this.validator = validator;
	}

	@Override
	public Optional<ResetPasswordToken> load(String tokenString) {
		Optional<UUID> valid = validator.getValidId(tokenString);
		if (valid.isEmpty())
			return Optional.empty();

		Optional<ResetPasswordTokenJPA> token = tokenRepo.findByToken(valid.get());
		if (token.isEmpty())
			return Optional.empty();

		return Optional.of(mapper.map(token.get()));
	}

}
