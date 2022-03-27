package it.aldinucci.todoapp.webcommons.mapper;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.in.dto.NewUserDTOIn;
import it.aldinucci.todoapp.mapper.AppGenericMapper;
import it.aldinucci.todoapp.webcommons.dto.RegisterUserDto;

@Component
public class RegisterUserDtoToNewUserDTOInMapper implements AppGenericMapper<RegisterUserDto, NewUserDTOIn>{

	@Override
	public NewUserDTOIn map(RegisterUserDto model) {
		return new NewUserDTOIn(model.getUsername(), model.getEmail(), model.getPassword());
	}

}
