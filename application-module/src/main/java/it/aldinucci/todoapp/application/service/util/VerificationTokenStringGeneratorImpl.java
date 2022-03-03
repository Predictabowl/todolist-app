package it.aldinucci.todoapp.application.service.util;

import java.util.Calendar;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.out.DeleteVerificationTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadVerificationTokenDriverPort;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.util.RandomStringGenerator;

/**
 * With the current implementation there's a possibility (albeit remote) that the
 * generator will be stuck in a loop. It will happen if the token length is
 * not enough to generate unique strings.
 * @author piero
 *
 */
@Component
public class VerificationTokenStringGeneratorImpl implements VerificationTokenStringGenerator {

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
		while (tokenString.isEmpty()) {
			tokenString = randStringGen.generate(length);
			token = loadToken.load(tokenString);
			if (!token.isEmpty()) {
				if (token.get().isExpired(Calendar.getInstance().getTime()))
					deleteToken.delete(tokenString);
				else
					tokenString = "";
			}
		}
		return tokenString;
	}	
}
