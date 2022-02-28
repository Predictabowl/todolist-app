package it.aldinucci.todoapp.application.mapper;

import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.application.port.in.dto.NewUserDTOIn;
import it.aldinucci.todoapp.application.port.out.dto.NewUserDTOOut;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class UserMapperInOut implements AppGenericMapper<NewUserDTOIn, NewUserDTOOut> {
	
	@Override
	public NewUserDTOOut map(NewUserDTOIn model) {
		return new NewUserDTOOut(
				model.getUsername(),
				model.getEmail(),
				model.getPassword());
	}

}
