package it.aldinucci.todoapp.application.service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.out.CreateVerificationTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.VerificationTokenDTOOut;
import it.aldinucci.todoapp.application.util.ApplicationPropertyNames;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exceptions.AppUserNotFoundException;

@Service
public class CreateVerificationTokenServiceImpl implements CreateVerificationTokenService {
	
	public static final int DEFAULT_TOKEN_LENGTH = 64;
	
	private CreateVerificationTokenDriverPort createVerificationToken;
	private VerificationTokenStringGenerator tokenStringGenerator;
	private VerificationTokenExpiryDateGenerator dateGenerator;
	private Environment env;

	@Autowired
	public CreateVerificationTokenServiceImpl(CreateVerificationTokenDriverPort createVerificationToken,
			VerificationTokenStringGenerator tokenStringGenerator, VerificationTokenExpiryDateGenerator dateGenerator,
			Environment env) {
		this.createVerificationToken = createVerificationToken;
		this.tokenStringGenerator = tokenStringGenerator;
		this.dateGenerator = dateGenerator;
		this.env = env;
	}


	@Override
	public VerificationToken create(User user) throws AppUserNotFoundException{
		Integer length = env.getProperty(
				ApplicationPropertyNames.VERIFICATION_TOKEN_LENGTH.get(),
				Integer.class,
				DEFAULT_TOKEN_LENGTH);
		VerificationTokenDTOOut tokenDto = new VerificationTokenDTOOut(
				tokenStringGenerator.generate(length), dateGenerator.generate(), user.getEmail());
		
		return createVerificationToken.create(tokenDto);
	}

}
