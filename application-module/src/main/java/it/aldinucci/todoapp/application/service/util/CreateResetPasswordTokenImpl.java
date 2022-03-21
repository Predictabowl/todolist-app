package it.aldinucci.todoapp.application.service.util;

import static it.aldinucci.todoapp.config.ApplicationPropertyNames.RESET_PASSWORD_TOKEN_DURATION;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.out.CreateResetPasswordTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.ResetPasswordTokenData;
import it.aldinucci.todoapp.domain.ResetPasswordToken;
import it.aldinucci.todoapp.exception.AppResetPasswordTokenAlreadyExistsException;

@Component
public class CreateResetPasswordTokenImpl implements CreateResetPasswordToken {

	public static final int DEFAULT_TOKEN_MINUTES_DURATION = 60;
	
	private CreateResetPasswordTokenDriverPort createResetToken;
	private UniqueResetPasswordTokenGenerator tokenStringGenerator;
	private TokenExpiryDateGenerator dateGenerator;
	
	
	@Autowired
	public CreateResetPasswordTokenImpl(CreateResetPasswordTokenDriverPort createResetToken,
			UniqueResetPasswordTokenGenerator tokenStringGenerator, TokenExpiryDateGenerator dateGenerator) {
		super();
		this.createResetToken = createResetToken;
		this.tokenStringGenerator = tokenStringGenerator;
		this.dateGenerator = dateGenerator;
	}



	@Override
	public ResetPasswordToken create(String email) throws AppResetPasswordTokenAlreadyExistsException {
		return createResetToken.create(new ResetPasswordTokenData(
				tokenStringGenerator.generate(), 
				dateGenerator.generate(RESET_PASSWORD_TOKEN_DURATION, DEFAULT_TOKEN_MINUTES_DURATION),
				email));
	}

}
