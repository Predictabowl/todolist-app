package it.aldinucci.todoapp.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class UserJpaToUserMapper implements AppGenericMapper<UserJPA, User>{

	@Override
	public User map(UserJPA model) {
		return new User(model.getEmail(), model.getUsername(), model.getPassword(), model.isEnabled());
	}

}
