package it.aldinucci.todoapp.application.service;

import java.util.Calendar;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.GetOrCreateVerificationTokenUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.DeleteVerificatinTokenByUserDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadUserByEmailDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadVerificationTokenByEmailDriverPort;
import it.aldinucci.todoapp.application.service.util.CreateVerificationToken;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exception.AppUserEmailAlreadyVerifiedException;

@Service
@Transactional
public class GetOrCreateVerificationTokenService implements GetOrCreateVerificationTokenUsePort {
	
	private LoadUserByEmailDriverPort loadUser;
	private LoadVerificationTokenByEmailDriverPort loadToken;
	private DeleteVerificatinTokenByUserDriverPort deleteToken;
	private CreateVerificationToken createToken;


	@Autowired
	public GetOrCreateVerificationTokenService(LoadUserByEmailDriverPort loadUser,
			LoadVerificationTokenByEmailDriverPort loadToken, DeleteVerificatinTokenByUserDriverPort deleteToken,
			CreateVerificationToken createToken) {
		super();
		this.loadUser = loadUser;
		this.loadToken = loadToken;
		this.deleteToken = deleteToken;
		this.createToken = createToken;
	}


	@Override
	public Optional<VerificationToken> get(UserIdDTO userId) throws AppUserEmailAlreadyVerifiedException{
		Optional<User> optionalUser = loadUser.load(userId.getEmail());
		if(optionalUser.isEmpty())
			return Optional.empty();
		
		if (optionalUser.get().isEnabled())
			throw new AppUserEmailAlreadyVerifiedException("Email "+userId.getEmail()+" is already verified.");
		
		Optional<VerificationToken> token = loadToken.load(userId.getEmail());
		if (token.isPresent()) {
			if(!token.get().isExpired(Calendar.getInstance().getTime()))
				return token;
			deleteToken.delete(userId.getEmail());
		}
		
		return Optional.of(createToken.create(userId.getEmail()));
	}

}
