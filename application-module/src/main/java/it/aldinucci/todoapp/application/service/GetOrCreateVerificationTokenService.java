package it.aldinucci.todoapp.application.service;

import java.util.Calendar;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.GetOrCreateVerificationTokenUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.DeleteVerificatinTokenByUserDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadUserByIdDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadVerificationTokenByEmailDriverPort;
import it.aldinucci.todoapp.application.service.util.CreateVerificationToken;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exception.AppUserEmailAlreadyVerifiedException;

@Service
@Transactional
public class GetOrCreateVerificationTokenService implements GetOrCreateVerificationTokenUsePort {
	
	private final LoadUserByIdDriverPort loadUser;
	private final LoadVerificationTokenByEmailDriverPort loadToken;
	private final DeleteVerificatinTokenByUserDriverPort deleteToken;
	private final CreateVerificationToken createToken;


	public GetOrCreateVerificationTokenService(LoadUserByIdDriverPort loadUser,
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
		Optional<User> optionalUser = loadUser.load(userId.getId());
		if(optionalUser.isEmpty())
			return Optional.empty();
		
		if (optionalUser.get().isEnabled())
			throw new AppUserEmailAlreadyVerifiedException("Email "+userId.getId()+" is already verified.");
		
		Optional<VerificationToken> token = loadToken.load(userId.getId());
		if (token.isPresent()) {
			if(!token.get().isExpired(Calendar.getInstance().getTime()))
				return token;
			deleteToken.delete(userId.getId());
		}
		
		return Optional.of(createToken.create(userId.getId()));
	}

}
