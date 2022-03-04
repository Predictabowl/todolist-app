package it.aldinucci.todoapp.application.service;

import static it.aldinucci.todoapp.util.ApplicationPropertyNames.VERIFICATION_TOKEN_LENGTH;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.CreateVerificationTokenUsePort;
import it.aldinucci.todoapp.application.port.out.CreateVerificationTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.DeleteVerificatinTokenByUserDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.VerificationTokenDTOOut;
import it.aldinucci.todoapp.application.service.util.VerificationTokenExpiryDateGenerator;
import it.aldinucci.todoapp.application.service.util.VerificationTokenStringGenerator;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exceptions.AppUserNotFoundException;
import it.aldinucci.todoapp.util.AppPropertiesReader;

@Service
@Transactional
public class CreateVerificationTokenService implements CreateVerificationTokenUsePort {
	
	public static final int DEFAULT_TOKEN_LENGTH = 64;
	
	private CreateVerificationTokenDriverPort createVerificationToken;
	private DeleteVerificatinTokenByUserDriverPort deleteVerificationToken;
	private VerificationTokenStringGenerator tokenStringGenerator;
	private VerificationTokenExpiryDateGenerator dateGenerator;
	private AppPropertiesReader propReader;

	@Autowired
	public CreateVerificationTokenService(CreateVerificationTokenDriverPort createVerificationToken,
			DeleteVerificatinTokenByUserDriverPort deleteVerificationToken,
			VerificationTokenStringGenerator tokenStringGenerator, VerificationTokenExpiryDateGenerator dateGenerator,
			AppPropertiesReader propReader) {
		this.createVerificationToken = createVerificationToken;
		this.deleteVerificationToken = deleteVerificationToken;
		this.tokenStringGenerator = tokenStringGenerator;
		this.dateGenerator = dateGenerator;
		this.propReader = propReader;
	}


	@Override
	public VerificationToken create(User user) throws AppUserNotFoundException{
		int length = propReader.get(VERIFICATION_TOKEN_LENGTH, Integer.class, DEFAULT_TOKEN_LENGTH);
		
		deleteVerificationToken.delete(user.getEmail());
		VerificationTokenDTOOut tokenDto = new VerificationTokenDTOOut(
				tokenStringGenerator.generate(length),
				dateGenerator.generate(),
				user.getEmail());
		
		return createVerificationToken.create(tokenDto);
	}

}
