package it.aldinucci.todoapp.application.service;

import java.util.Calendar;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.VerifyUserEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.StringTokenDTOIn;
import it.aldinucci.todoapp.application.port.out.DeleteVerificationTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadUserByEmailDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadVerificationTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.UpdateUserDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.UserData;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Service
@Transactional
public class VerifyUserEmailService implements VerifyUserEmailUsePort {

	private LoadVerificationTokenDriverPort loadToken;
	private LoadUserByEmailDriverPort loadUser;
	private UpdateUserDriverPort updateUser;
	private DeleteVerificationTokenDriverPort deleteToken;
	private AppGenericMapper<User, UserData> mapper; 

	@Autowired
	public VerifyUserEmailService(LoadVerificationTokenDriverPort loadToken, LoadUserByEmailDriverPort loadUser,
			UpdateUserDriverPort updateUser, DeleteVerificationTokenDriverPort deleteToken,
			AppGenericMapper<User, UserData> mapper) {
		super();
		this.loadToken = loadToken;
		this.loadUser = loadUser;
		this.updateUser = updateUser;
		this.deleteToken = deleteToken;
		this.mapper = mapper;
	}


	@Override
	public boolean verify(StringTokenDTOIn tokenDto) throws AppUserNotFoundException{
		Optional<VerificationToken> loadedToken = loadToken.load(tokenDto.getToken());
		if (loadedToken.isEmpty())
			return false;
		
		VerificationToken verificationToken = loadedToken.get();
		deleteToken.delete(verificationToken.getToken());
		if (verificationToken.isExpired(Calendar.getInstance().getTime())) 
			return false;
		
		User user = loadUser.load(verificationToken.getUserEmail())
				.orElseThrow(() -> new AppUserNotFoundException(
						"Data Integrity compromised, could not find user linked with token with email: "
								+ verificationToken.getUserEmail()));
		
		user.setEnabled(true);
		updateUser.update(mapper.map(user));
		return true;
	}

}
