package it.aldinucci.todoapp.application.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.UpdateUserDataUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserDataDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadUserByIdDriverPort;
import it.aldinucci.todoapp.application.port.out.UpdateUserDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.UserData;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Service
@Transactional
public class UpdateUserDataService implements UpdateUserDataUsePort {

	private LoadUserByIdDriverPort loadUser;
	private UpdateUserDriverPort updateUser;
	private AppGenericMapper<User, UserData> mapper;

	@Autowired
	public UpdateUserDataService(LoadUserByIdDriverPort loadUser, UpdateUserDriverPort updateUser,
			AppGenericMapper<User, UserData> mapper) {
		super();
		this.loadUser = loadUser;
		this.updateUser = updateUser;
		this.mapper = mapper;
	}


	@Override
	public Optional<User> update(UserIdDTO userId, UserDataDTOIn userData) {
		Optional<User> optional = loadUser.load(userId.getId());
		if(optional.isEmpty())
			return Optional.empty();
		
		User user = optional.get();
		user.setUsername(userData.getUsername());
		
		return Optional.of(updateUser.update(mapper.map(user)));
	}

}
