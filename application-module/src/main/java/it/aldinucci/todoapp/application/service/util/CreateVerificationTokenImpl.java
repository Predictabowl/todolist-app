package it.aldinucci.todoapp.application.service.util;

import static it.aldinucci.todoapp.config.ApplicationPropertyNames.VERIFICATION_TOKEN_DURATION;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.out.CreateUserVerificationTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.VerificationTokenData;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exception.AppVerificationTokenAlreadyExistsException;

@Component
public class CreateVerificationTokenImpl implements CreateVerificationToken {

	public static final int DEFAULT_TOKEN_MINUTES_DURATION = 1440;
	
	private CreateUserVerificationTokenDriverPort createVerificationToken;
	private UniqueVerificationTokenGenerator tokenStringGenerator;
	private TokenExpiryDateGenerator dateGenerator;
	
	@Autowired
	public CreateVerificationTokenImpl(CreateUserVerificationTokenDriverPort createVerificationToken,
			UniqueVerificationTokenGenerator tokenStringGenerator, TokenExpiryDateGenerator dateGenerator) {
		super();
		this.createVerificationToken = createVerificationToken;
		this.tokenStringGenerator = tokenStringGenerator;
		this.dateGenerator = dateGenerator;
	}

	@Override
	public VerificationToken create(String email) throws AppVerificationTokenAlreadyExistsException {
		VerificationTokenData tokenDto = new VerificationTokenData(
				tokenStringGenerator.generate(),
				dateGenerator.generate(VERIFICATION_TOKEN_DURATION, DEFAULT_TOKEN_MINUTES_DURATION),
				email);
		return createVerificationToken.create(tokenDto);
	}

}
