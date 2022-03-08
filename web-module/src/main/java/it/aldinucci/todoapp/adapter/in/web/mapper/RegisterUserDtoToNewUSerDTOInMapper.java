package it.aldinucci.todoapp.adapter.in.web.mapper;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.in.web.dto.RegisterUserDto;
import it.aldinucci.todoapp.application.port.in.dto.NewUserDTOIn;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class RegisterUserDtoToNewUSerDTOInMapper implements AppGenericMapper<RegisterUserDto, NewUserDTOIn>{

	@Override
	public NewUserDTOIn map(RegisterUserDto model) {
		return new NewUserDTOIn(model.getUsername(), model.getEmail(), model.getPassword());
	}

}
