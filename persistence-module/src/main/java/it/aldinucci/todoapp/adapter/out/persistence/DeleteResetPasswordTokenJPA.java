package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ResetPasswordTokenJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ResetPasswordTokenJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.util.ValidateId;
import it.aldinucci.todoapp.application.port.out.DeleteRestPasswordTokenDriverPort;

@Component
public class DeleteResetPasswordTokenJPA implements DeleteRestPasswordTokenDriverPort {

	private ResetPasswordTokenJPARepository tokenRepo;
	private ValidateId<UUID> validator;

	@Autowired
	public DeleteResetPasswordTokenJPA(ResetPasswordTokenJPARepository tokenRepo, ValidateId<UUID> validator) {
		super();
		this.tokenRepo = tokenRepo;
		this.validator = validator;
	}

	@Override
	public void delete(String token) {
		if (validator.isValid(token)) {
			Optional<ResetPasswordTokenJPA> optional = tokenRepo.findByToken(validator.getId());
			if (optional.isPresent())
				tokenRepo.delete(optional.get());
		}
	}

}
