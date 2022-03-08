package it.aldinucci.todoapp.application.service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.out.CreateUserVerificationTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.VerificationTokenData;
import it.aldinucci.todoapp.domain.VerificationToken;

@Component
public class CreateVerificationTokenImpl implements CreateVerificationToken {

	private CreateUserVerificationTokenDriverPort createVerificationToken;
	private UniqueVerificationTokenGenerator tokenStringGenerator;
	private VerificationTokenExpiryDateGenerator dateGenerator;
	
	@Autowired
	public CreateVerificationTokenImpl(CreateUserVerificationTokenDriverPort createVerificationToken,
			UniqueVerificationTokenGenerator tokenStringGenerator, VerificationTokenExpiryDateGenerator dateGenerator) {
		super();
		this.createVerificationToken = createVerificationToken;
		this.tokenStringGenerator = tokenStringGenerator;
		this.dateGenerator = dateGenerator;
	}

	@Override
	public VerificationToken create(String email) {
		VerificationTokenData tokenDto = new VerificationTokenData(
				tokenStringGenerator.generate(),
				dateGenerator.generate(),
				email);
		return createVerificationToken.create(tokenDto);
	}

}
