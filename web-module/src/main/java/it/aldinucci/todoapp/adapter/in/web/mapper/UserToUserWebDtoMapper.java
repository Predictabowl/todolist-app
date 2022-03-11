package it.aldinucci.todoapp.adapter.in.web.mapper;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.in.web.dto.UserWebDto;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class UserToUserWebDtoMapper implements AppGenericMapper<User, UserWebDto>{

	@Override
	public UserWebDto map(User model) {
		return new UserWebDto(model.getUsername(), model.getEmail());
	}

}
