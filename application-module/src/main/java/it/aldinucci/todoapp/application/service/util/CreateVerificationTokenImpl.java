package it.aldinucci.todoapp.application.service.util;

import static it.aldinucci.todoapp.config.ApplicationPropertyNames.VERIFICATION_TOKEN_DURATION;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.out.CreateUserVerificationTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.VerificationTokenData;
import it.aldinucci.todoapp.domain.VerificationToken;

@Component
public class CreateVerificationTokenImpl implements CreateVerificationToken {

	public static final int DEFAULT_TOKEN_MINUTES_DURATION = 1440;
	
	private CreateUserVerificationTokenDriverPort createVerificationToken;
	private TokenExpiryDateGenerator dateGenerator;
	
	public CreateVerificationTokenImpl(CreateUserVerificationTokenDriverPort createVerificationToken,
			TokenExpiryDateGenerator dateGenerator) {
		super();
		this.createVerificationToken = createVerificationToken;
		this.dateGenerator = dateGenerator;
	}

	@Override
	public VerificationToken create(String email){
		VerificationTokenData tokenDto = new VerificationTokenData(
				dateGenerator.generate(VERIFICATION_TOKEN_DURATION, DEFAULT_TOKEN_MINUTES_DURATION),
				email);
		return createVerificationToken.create(tokenDto);
	}

}
