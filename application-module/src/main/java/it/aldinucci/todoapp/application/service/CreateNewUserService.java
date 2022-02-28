package it.aldinucci.todoapp.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.CreateUserUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewUserDTOIn;
import it.aldinucci.todoapp.application.port.out.CreateUserDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.NewUserDTOOut;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Service
public class CreateNewUserService implements CreateUserUsePort{

	private AppGenericMapper<NewUserDTOIn, NewUserDTOOut> mapper;
	private CreateUserDriverPort createUser;
	private PasswordEncoder encoder;

	
	@Autowired
	public CreateNewUserService(AppGenericMapper<NewUserDTOIn, NewUserDTOOut> mapper, CreateUserDriverPort createUser,
			PasswordEncoder encoder) {
		this.mapper = mapper;
		this.createUser = createUser;
		this.encoder = encoder;
	}


	@Override
	public User create(NewUserDTOIn newUser) {
		NewUserDTOOut newUserOut = mapper.map(newUser);
		newUserOut.setPassword(encoder.encode(newUserOut.getPassword()));
		return createUser.create(newUserOut);
	}

}
