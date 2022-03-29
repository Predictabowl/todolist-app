package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.VerificationTokenJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.VerificationTokenJPARepository;
import it.aldinucci.todoapp.application.port.out.DeleteVerificationTokenDriverPort;
import it.aldinucci.todoapp.util.ValidationUtils;

@Component
public class DeleteVerificationTokenJPA implements DeleteVerificationTokenDriverPort{

	private VerificationTokenJPARepository tokenRepo;
	
	@Autowired
	public DeleteVerificationTokenJPA(VerificationTokenJPARepository tokenRepo) {
		this.tokenRepo = tokenRepo;
	}


	@Override
	public void delete(String tokenCode){
		if (ValidationUtils.isValidUUID(tokenCode)) {
			Optional<VerificationTokenJPA> optionalToken  = tokenRepo.findByToken(UUID.fromString(tokenCode));
			if(optionalToken.isPresent()) {
				tokenRepo.delete(optionalToken.get());
			}
		}
	}

}
