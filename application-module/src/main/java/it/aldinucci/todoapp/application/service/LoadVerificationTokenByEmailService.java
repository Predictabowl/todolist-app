package it.aldinucci.todoapp.application.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.LoadVerificationTokenByEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadVerificationTokenByEmailDriverPort;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;

@Service
@Transactional
public class LoadVerificationTokenByEmailService implements LoadVerificationTokenByEmailUsePort{

	private LoadVerificationTokenByEmailDriverPort loadToken;
	
	@Autowired
	public LoadVerificationTokenByEmailService(LoadVerificationTokenByEmailDriverPort loadToken) {
		super();
		this.loadToken = loadToken;
	}


	@Override
	public Optional<VerificationToken> load(UserIdDTO userId) throws AppUserNotFoundException {
		return loadToken.load(userId.getEmail());
	}

}
