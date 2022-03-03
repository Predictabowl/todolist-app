package it.aldinucci.todoapp.application.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.CreateUserUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewUserDTOIn;
import it.aldinucci.todoapp.application.port.out.CreateUserDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadUserByEmailDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.NewUserDTOOut;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exceptions.AppEmailAlreadyRegisteredException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Service
@Transactional
public class CreateNewUserService implements CreateUserUsePort{

	private AppGenericMapper<NewUserDTOIn, NewUserDTOOut> mapper;
	private CreateUserDriverPort createUser;
	private PasswordEncoder encoder;
	private LoadUserByEmailDriverPort loadUser;

	@Autowired
	public CreateNewUserService(AppGenericMapper<NewUserDTOIn, NewUserDTOOut> mapper, CreateUserDriverPort createUser,
			PasswordEncoder encoder, LoadUserByEmailDriverPort loadUser) {
		super();
		this.mapper = mapper;
		this.createUser = createUser;
		this.encoder = encoder;
		this.loadUser = loadUser;
	}



	@Override
	public User create(NewUserDTOIn newUser) throws AppEmailAlreadyRegisteredException{
		String email = newUser.getEmail();
		Optional<User> oldUser = loadUser.load(email);
		if(oldUser.isPresent())
			throw new AppEmailAlreadyRegisteredException("There's already an user registered with the email: "+email);
		NewUserDTOOut newUserOut = mapper.map(newUser);
		newUserOut.setPassword(encoder.encode(newUserOut.getPassword()));
		return createUser.create(newUserOut);
	}

}
