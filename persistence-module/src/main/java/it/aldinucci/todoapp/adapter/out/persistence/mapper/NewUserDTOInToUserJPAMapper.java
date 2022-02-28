package it.aldinucci.todoapp.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.application.port.out.dto.NewUserDTOOut;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class NewUserDTOInToUserJPAMapper implements AppGenericMapper<NewUserDTOOut, UserJPA>{

	@Override
	public UserJPA map(NewUserDTOOut model) {
		return new UserJPA(model.getEmail(), model.getUsername(), model.getPassword());
	}

}
