package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ResetPasswordTokenJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ResetPasswordTokenJPARepository;
import it.aldinucci.todoapp.application.port.out.DeleteRestPasswordTokenDriverPort;
import it.aldinucci.todoapp.util.ValidationUtils;

@Component
public class DeleteResetPasswordTokenJPA implements DeleteRestPasswordTokenDriverPort{

	private ResetPasswordTokenJPARepository tokenRepo;
	
	@Autowired
	public DeleteResetPasswordTokenJPA(ResetPasswordTokenJPARepository tokenRepo) {
		super();
		this.tokenRepo = tokenRepo;
	}


	@Override
	public void delete(String token) {
		if (ValidationUtils.isValidUUID(token)) {
			Optional<ResetPasswordTokenJPA> optional = tokenRepo.findByToken(UUID.fromString(token));
			if(optional.isPresent())
				tokenRepo.delete(optional.get());
		}
	}

}
