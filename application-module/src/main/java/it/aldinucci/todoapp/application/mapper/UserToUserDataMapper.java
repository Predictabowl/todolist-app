package it.aldinucci.todoapp.application.mapper;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.out.dto.UserData;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class UserToUserDataMapper implements AppGenericMapper<User, UserData>{

	@Override
	public UserData map(User model) {
		return new UserData(model.getUsername(), model.getEmail(), model.getPassword(), model.isEnabled());
	}

}
