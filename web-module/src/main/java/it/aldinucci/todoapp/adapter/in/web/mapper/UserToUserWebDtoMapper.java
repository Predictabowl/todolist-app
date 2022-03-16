package it.aldinucci.todoapp.adapter.in.web.mapper;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.mapper.AppGenericMapper;
import it.aldinucci.todoapp.webcommons.dto.UserWebDto;

@Component
public class UserToUserWebDtoMapper implements AppGenericMapper<User, UserWebDto>{

	@Override
	public UserWebDto map(User model) {
		return new UserWebDto(model.getUsername(), model.getEmail());
	}

}
