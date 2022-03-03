package it.aldinucci.todoapp.application.service.util;

import java.util.Calendar;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.out.DeleteVerificationTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadVerificationTokenDriverPort;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exceptions.AppCouldNotGenerateVerificationTokenException;
import it.aldinucci.todoapp.util.RandomStringGenerator;

/**
 * With the current implementation there's a possibility (albeit remote) that the
 * generator will not be able to generate a token.
 * @author piero
 *
 */
@Component
public class VerificationTokenStringGeneratorImpl implements VerificationTokenStringGenerator {

	private static final int MAX_LOOP_NUMBER = 2048;
	
	private RandomStringGenerator randStringGen;
	private LoadVerificationTokenDriverPort loadToken;
	private DeleteVerificationTokenDriverPort deleteToken;
	
	@Autowired
	public VerificationTokenStringGeneratorImpl(RandomStringGenerator randStringGen,
			LoadVerificationTokenDriverPort loadToken, DeleteVerificationTokenDriverPort deleteToken) {
		this.randStringGen = randStringGen;
		this.loadToken = loadToken;
		this.deleteToken = deleteToken;
	}


	@Override
	public String generate(int length) {
		String tokenString = "";
		Optional<VerificationToken> token;
		int i = 0;
		while (tokenString.isEmpty() && i < MAX_LOOP_NUMBER) {
			tokenString = randStringGen.generate(length);
			token = loadToken.load(tokenString);
			if (!token.isEmpty()) {
				if (token.get().isExpired(Calendar.getInstance().getTime()))
					deleteToken.delete(tokenString);
				else
					tokenString = "";
			}
			i++;
		}
		if (i >= MAX_LOOP_NUMBER && tokenString.isEmpty())
			throw new AppCouldNotGenerateVerificationTokenException();
		
		return tokenString;
	}	
}
