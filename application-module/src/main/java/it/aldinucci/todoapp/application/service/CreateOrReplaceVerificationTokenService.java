package it.aldinucci.todoapp.application.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.CreateOrReplaceVerificationTokenUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.DeleteVerificatinTokenByUserDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadUserByEmailDriverPort;
import it.aldinucci.todoapp.application.service.util.CreateVerificationToken;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exceptions.AppUserNotFoundException;

@Service
@Transactional
public class CreateOrReplaceVerificationTokenService implements CreateOrReplaceVerificationTokenUsePort {
	
	private LoadUserByEmailDriverPort loadUser;
	private DeleteVerificatinTokenByUserDriverPort deleteToken;
	private CreateVerificationToken createToken;


	@Autowired
	public CreateOrReplaceVerificationTokenService(LoadUserByEmailDriverPort loadUser,
			DeleteVerificatinTokenByUserDriverPort deleteToken, CreateVerificationToken createToken) {
		super();
		this.loadUser = loadUser;
		this.deleteToken = deleteToken;
		this.createToken = createToken;
	}



	@Override
	public VerificationToken create(UserIdDTO userId) throws AppUserNotFoundException{
		User user = loadUser.load(userId.getEmail()).orElseThrow(() ->
				new AppUserNotFoundException("Could not find user with email: " + userId.getEmail()));
		
		if (user.isEnabled())
			return null;
		
		deleteToken.delete(userId.getEmail());
		
		return createToken.create(userId.getEmail());
	}

}
