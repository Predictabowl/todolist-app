package it.aldinucci.todoapp.application.service.util;

import java.util.Calendar;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.out.DeleteRestPasswordTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadResetPasswordTokenDriverPort;
import it.aldinucci.todoapp.domain.ResetPasswordToken;
import it.aldinucci.todoapp.exception.AppCouldNotGenerateTokenException;

@Component
public class UniqueResetPasswordTokenGeneratorImpl implements UniqueResetPasswordTokenGenerator {

	private static final int MAX_LOOP_NUMBER = 2048;
	
	private TokenStringGenerator randStringGen;
	private LoadResetPasswordTokenDriverPort loadToken;
	private DeleteRestPasswordTokenDriverPort deleteToken;
	
	
	@Autowired
	public UniqueResetPasswordTokenGeneratorImpl(TokenStringGenerator randStringGen,
			LoadResetPasswordTokenDriverPort loadToken, DeleteRestPasswordTokenDriverPort deleteToken) {
		super();
		this.randStringGen = randStringGen;
		this.loadToken = loadToken;
		this.deleteToken = deleteToken;
	}

	@Override
	public String generate() {
		String tokenCode = "";
		int i = 0;
		while(tokenCode.isEmpty() && i < MAX_LOOP_NUMBER) {
			tokenCode = randStringGen.generate();
			Optional<ResetPasswordToken> oldToken = loadToken.load(tokenCode);
			if(oldToken.isPresent()) {
				if (oldToken.get().isExpired(Calendar.getInstance().getTime()))
					deleteToken.delete(tokenCode);
				else
					tokenCode = "";
			}
			i++;
		}
		
		if(i >= MAX_LOOP_NUMBER)
			throw new AppCouldNotGenerateTokenException();
		return tokenCode;
	}

}
