package it.aldinucci.todoapp.application.service;

import java.util.Calendar;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.ChangeUserPasswordUsePort;
import it.aldinucci.todoapp.application.port.in.dto.StringTokenDTOIn;
import it.aldinucci.todoapp.application.port.in.model.AppPassword;
import it.aldinucci.todoapp.application.port.out.DeleteRestPasswordTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadResetPasswordTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadUserByIdDriverPort;
import it.aldinucci.todoapp.application.port.out.UpdateUserDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.UserData;
import it.aldinucci.todoapp.domain.ResetPasswordToken;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;
import it.aldinucci.todoapp.util.AppPasswordEncoder;

@Service
@Transactional
public class ChangeUserPasswordService implements ChangeUserPasswordUsePort {

	private final LoadResetPasswordTokenDriverPort loadToken;
	private final LoadUserByIdDriverPort loadUser;
	private final UpdateUserDriverPort updateUser;
	private final DeleteRestPasswordTokenDriverPort deleteToken;
	private final AppPasswordEncoder encoder;
	private final AppGenericMapper<User, UserData> mapper;


	public ChangeUserPasswordService(LoadResetPasswordTokenDriverPort loadToken, LoadUserByIdDriverPort loadUser,
			UpdateUserDriverPort updateUser, DeleteRestPasswordTokenDriverPort deleteToken, AppPasswordEncoder encoder,
			AppGenericMapper<User, UserData> mapper) {
		super();
		this.loadToken = loadToken;
		this.loadUser = loadUser;
		this.updateUser = updateUser;
		this.deleteToken = deleteToken;
		this.encoder = encoder;
		this.mapper = mapper;
	}


	@Override
	public boolean change(StringTokenDTOIn resetToken, AppPassword newPassword) {
		Optional<ResetPasswordToken> optional = loadToken.load(resetToken.getToken());
		if (optional.isEmpty())
			return false;
		
		ResetPasswordToken token = optional.get();
		deleteToken.delete(token.getToken());
		if (token.isExpired(Calendar.getInstance().getTime()))
			return false;
		
		User user = loadUser.load(token.getUserEmail()).orElseThrow(() 
				-> new AppUserNotFoundException("Could not find user with email: "+token.getUserEmail()));
		
		user.setPassword(encoder.encode(newPassword.getPassword()));
		updateUser.update(mapper.map(user));
		return true;
	}

}
