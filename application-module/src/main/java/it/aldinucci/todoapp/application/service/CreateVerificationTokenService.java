package it.aldinucci.todoapp.application.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.CreateVerificationTokenUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.CreateVerificationTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.DeleteVerificatinTokenByUserDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.VerificationTokenDTOOut;
import it.aldinucci.todoapp.application.service.util.UniqueVerificationTokenGenerator;
import it.aldinucci.todoapp.application.service.util.VerificationTokenExpiryDateGenerator;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exceptions.AppUserNotFoundException;

@Service
@Transactional
public class CreateVerificationTokenService implements CreateVerificationTokenUsePort {
	
	private CreateVerificationTokenDriverPort createVerificationToken;
	private DeleteVerificatinTokenByUserDriverPort deleteVerificationToken;
	private UniqueVerificationTokenGenerator tokenStringGenerator;
	private VerificationTokenExpiryDateGenerator dateGenerator;

	@Autowired
	public CreateVerificationTokenService(CreateVerificationTokenDriverPort createVerificationToken,
			DeleteVerificatinTokenByUserDriverPort deleteVerificationToken,
			UniqueVerificationTokenGenerator tokenStringGenerator, VerificationTokenExpiryDateGenerator dateGenerator) {
		this.createVerificationToken = createVerificationToken;
		this.deleteVerificationToken = deleteVerificationToken;
		this.tokenStringGenerator = tokenStringGenerator;
		this.dateGenerator = dateGenerator;
	}


	@Override
	public VerificationToken create(UserIdDTO userId) throws AppUserNotFoundException{
		deleteVerificationToken.delete(userId.getEmail());
		VerificationTokenDTOOut tokenDto = new VerificationTokenDTOOut(
				tokenStringGenerator.generate(),
				dateGenerator.generate(),
				userId.getEmail());
		
		return createVerificationToken.create(tokenDto);
	}

}
