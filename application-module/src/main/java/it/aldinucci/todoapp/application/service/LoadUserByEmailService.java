package it.aldinucci.todoapp.application.service;

import java.util.Calendar;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.LoadUserByEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.DeleteUserByEmailDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadUserByEmailDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadVerificationTokenByEmailDriverPort;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.domain.VerificationToken;

@Service
@Transactional
public class LoadUserByEmailService implements LoadUserByEmailUsePort{

	private LoadUserByEmailDriverPort loadUserPort;
	private LoadVerificationTokenByEmailDriverPort loadToken;
	private DeleteUserByEmailDriverPort deleteUser;
	
	
	@Autowired
	public LoadUserByEmailService(LoadUserByEmailDriverPort loadUserPort,
			LoadVerificationTokenByEmailDriverPort loadToken, DeleteUserByEmailDriverPort deleteUser) {
		super();
		this.loadUserPort = loadUserPort;
		this.loadToken = loadToken;
		this.deleteUser = deleteUser;
	}


	@Override
	public Optional<User> load(UserIdDTO id){
		Optional<User> user = loadUserPort.load(id.getEmail());
		if(user.isEmpty())
			return user;
		
		if(!user.get().isEnabled()) {
			Optional<VerificationToken> token = loadToken.load(id.getEmail());
			if(token.isEmpty() || token.get().isExpired(Calendar.getInstance().getTime())) {
				deleteUser.delete(id.getEmail());
				return Optional.empty();
			}
		}
		return user;
	}

}
