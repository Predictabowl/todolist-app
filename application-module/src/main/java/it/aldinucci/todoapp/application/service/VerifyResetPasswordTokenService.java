package it.aldinucci.todoapp.application.service;

import java.util.Calendar;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.VerifyResetPasswordTokenUsePort;
import it.aldinucci.todoapp.application.port.in.dto.StringTokenDTOIn;
import it.aldinucci.todoapp.application.port.out.DeleteRestPasswordTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadResetPasswordTokenDriverPort;
import it.aldinucci.todoapp.domain.ResetPasswordToken;

@Service
@Transactional
public class VerifyResetPasswordTokenService implements VerifyResetPasswordTokenUsePort {

	private LoadResetPasswordTokenDriverPort loadToken;
	private DeleteRestPasswordTokenDriverPort deleteToken;

	@Autowired
	public VerifyResetPasswordTokenService(LoadResetPasswordTokenDriverPort loadToken,
			DeleteRestPasswordTokenDriverPort deleteToken) {
		super();
		this.loadToken = loadToken;
		this.deleteToken = deleteToken;
	}

	@Override
	public boolean verify(StringTokenDTOIn token) {
		Optional<ResetPasswordToken> optional = loadToken.load(token.getToken());

		if (optional.isEmpty())
			return false;
		
		if (optional.get().isExpired(Calendar.getInstance().getTime())) {
			deleteToken.delete(token.getToken());
			return false;
		}

		return true;
	}

}
