package it.aldinucci.todoapp.application.service;

import java.util.Calendar;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.GetOrCreatePasswordResetTokenUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.DeleteRestPasswordTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadResetPasswordTokenByEmailDriverPort;
import it.aldinucci.todoapp.application.port.out.UserExistsDriverPort;
import it.aldinucci.todoapp.application.service.util.CreateResetPasswordToken;
import it.aldinucci.todoapp.domain.ResetPasswordToken;

@Service
@Transactional
public class GetOrCreateResetPasswordTokenService implements GetOrCreatePasswordResetTokenUsePort{

	private final UserExistsDriverPort userExists;
	private final LoadResetPasswordTokenByEmailDriverPort loadToken;
	private final DeleteRestPasswordTokenDriverPort deleteToken;
	private final CreateResetPasswordToken createToken;
	
	public GetOrCreateResetPasswordTokenService(UserExistsDriverPort userExists,
			LoadResetPasswordTokenByEmailDriverPort loadToken, DeleteRestPasswordTokenDriverPort deleteToken,
			CreateResetPasswordToken createToken) {
		super();
		this.userExists = userExists;
		this.loadToken = loadToken;
		this.deleteToken = deleteToken;
		this.createToken = createToken;
	}


	@Override
	public Optional<ResetPasswordToken> get(UserIdDTO userId) {
		if(!userExists.exists(userId.getId()))
			return Optional.empty();
		
		Optional<ResetPasswordToken> optional = loadToken.load(userId.getId());
		if(optional.isPresent()) {
			if(!optional.get().isExpired(Calendar.getInstance().getTime()))
				return optional;
			deleteToken.delete(optional.get().getToken());
		}
		
		return Optional.of(createToken.create(userId.getId()));
	}

}
