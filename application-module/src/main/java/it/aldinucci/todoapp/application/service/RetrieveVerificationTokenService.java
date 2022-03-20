package it.aldinucci.todoapp.application.service;

import java.util.Calendar;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.RetrieveVerificationTokenUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.DeleteVerificatinTokenByUserDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadUserByEmailDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadVerificationTokenByEmailDriverPort;
import it.aldinucci.todoapp.application.service.util.CreateVerificationToken;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exception.AppUserEmailAlreadyVerifiedException;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;

@Service
@Transactional
public class RetrieveVerificationTokenService implements RetrieveVerificationTokenUsePort {
	
	private LoadUserByEmailDriverPort loadUser;
	private LoadVerificationTokenByEmailDriverPort loadToken;
	private DeleteVerificatinTokenByUserDriverPort deleteToken;
	private CreateVerificationToken createToken;


	@Autowired
	public RetrieveVerificationTokenService(LoadUserByEmailDriverPort loadUser,
			LoadVerificationTokenByEmailDriverPort loadToken, DeleteVerificatinTokenByUserDriverPort deleteToken,
			CreateVerificationToken createToken) {
		super();
		this.loadUser = loadUser;
		this.loadToken = loadToken;
		this.deleteToken = deleteToken;
		this.createToken = createToken;
	}


	@Override
	public VerificationToken get(UserIdDTO userId){
		User user = loadUser.load(userId.getEmail()).orElseThrow(() ->
				new AppUserNotFoundException("Could not find user with email: " + userId.getEmail()));
		
		if (user.isEnabled())
			throw new AppUserEmailAlreadyVerifiedException("Email "+userId.getEmail()+" is already verified.");
		
		Optional<VerificationToken> token = loadToken.load(userId.getEmail());
		if (token.isPresent()) {
			if(token.get().isExpired(Calendar.getInstance().getTime()))
				deleteToken.delete(userId.getEmail());
			else
				return token.get();
		}
		
		return createToken.create(userId.getEmail());
	}

}
