package it.aldinucci.todoapp.application.service.util;

import static it.aldinucci.todoapp.config.ApplicationPropertyNames.RESET_PASSWORD_TOKEN_DURATION;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.out.CreateResetPasswordTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.ResetPasswordTokenData;
import it.aldinucci.todoapp.domain.ResetPasswordToken;

@Component
public class CreateResetPasswordTokenImpl implements CreateResetPasswordToken {

	public static final int DEFAULT_TOKEN_MINUTES_DURATION = 60;
	
	private CreateResetPasswordTokenDriverPort createResetToken;
	private TokenExpiryDateGenerator dateGenerator;
	
	
	@Autowired
	public CreateResetPasswordTokenImpl(CreateResetPasswordTokenDriverPort createResetToken,
			TokenExpiryDateGenerator dateGenerator) {
		super();
		this.createResetToken = createResetToken;
		this.dateGenerator = dateGenerator;
	}



	@Override
	public ResetPasswordToken create(String email){
		return createResetToken.create(new ResetPasswordTokenData(
				dateGenerator.generate(RESET_PASSWORD_TOKEN_DURATION, DEFAULT_TOKEN_MINUTES_DURATION),
				email));
	}

}
