package it.aldinucci.todoapp.application.mapper;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.in.dto.NewUserDTOIn;
import it.aldinucci.todoapp.application.port.out.dto.NewUserData;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class UserMapperInOut implements AppGenericMapper<NewUserDTOIn, NewUserData> {
	
	@Override
	public NewUserData map(NewUserDTOIn model) {
		return new NewUserData(
				model.getUsername(),
				model.getEmail(),
				model.getPassword());
	}

}
