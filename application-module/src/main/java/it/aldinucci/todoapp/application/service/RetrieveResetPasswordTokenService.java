package it.aldinucci.todoapp.application.service;

import java.util.Calendar;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.RetrievePasswordResetTokenUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.DeleteRestPasswordTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadResetPasswordTokenByEmailDriverPort;
import it.aldinucci.todoapp.application.service.util.CreateResetPasswordToken;
import it.aldinucci.todoapp.domain.ResetPasswordToken;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;

@Service
@Transactional
public class RetrieveResetPasswordTokenService implements RetrievePasswordResetTokenUsePort{

	private LoadResetPasswordTokenByEmailDriverPort loadToken;
	private DeleteRestPasswordTokenDriverPort deleteToken;
	private CreateResetPasswordToken createToken;
	
	@Autowired
	public RetrieveResetPasswordTokenService(LoadResetPasswordTokenByEmailDriverPort loadToken,
			DeleteRestPasswordTokenDriverPort deleteToken, CreateResetPasswordToken createToken) {
		super();
		this.loadToken = loadToken;
		this.deleteToken = deleteToken;
		this.createToken = createToken;
	}

	@Override
	public ResetPasswordToken get(UserIdDTO userId) throws AppUserNotFoundException {
		Optional<ResetPasswordToken> optional = loadToken.load(userId.getEmail());
		if(optional.isPresent()) {
			ResetPasswordToken oldToken = optional.get();
			if(oldToken.isExpired(Calendar.getInstance().getTime()))
				deleteToken.delete(oldToken.getToken());
			else
				return oldToken;
		}
		
		return createToken.create(userId.getEmail());
	}

}
