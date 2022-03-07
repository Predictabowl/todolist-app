package it.aldinucci.todoapp.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.application.port.out.dto.NewUserData;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class NewUserDTOInToUserJPAMapper implements AppGenericMapper<NewUserData, UserJPA>{

	@Override
	public UserJPA map(NewUserData model) {
		return new UserJPA(model.getEmail(), model.getUsername(), model.getPassword());
	}

}
