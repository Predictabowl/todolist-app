package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.VerificationTokenJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.VerificationTokenJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.util.ValidateId;
import it.aldinucci.todoapp.application.port.out.DeleteVerificationTokenDriverPort;

@Component
public class DeleteVerificationTokenJPA implements DeleteVerificationTokenDriverPort {

	private VerificationTokenJPARepository tokenRepo;
	private ValidateId<UUID> validator;

	@Autowired
	public DeleteVerificationTokenJPA(VerificationTokenJPARepository tokenRepo, ValidateId<UUID> validator) {
		super();
		this.tokenRepo = tokenRepo;
		this.validator = validator;
	}

	@Override
	public void delete(String tokenCode) {
		Optional<UUID> valid = validator.isValid(tokenCode);
		if (valid.isPresent()) {
			Optional<VerificationTokenJPA> optionalToken = tokenRepo.findByToken(valid.get());
			if (optionalToken.isPresent()) {
				tokenRepo.delete(optionalToken.get());
			}
		}
	}

}
