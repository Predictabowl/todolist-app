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
import it.aldinucci.todoapp.application.port.out.dto.NewUserData;
import it.aldinucci.todoapp.application.service.util.CreateVerificationToken;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exceptions.AppEmailAlreadyRegisteredException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Service
@Transactional
public class CreateNewUserService implements CreateUserUsePort{

	private AppGenericMapper<NewUserDTOIn, NewUserData> mapper;
	private CreateUserDriverPort createUser;
	private PasswordEncoder encoder;
	private LoadUserByEmailDriverPort loadUser;
	private CreateVerificationToken createToken;

	@Autowired
	public CreateNewUserService(AppGenericMapper<NewUserDTOIn, NewUserData> mapper, CreateUserDriverPort createUser,
			PasswordEncoder encoder, LoadUserByEmailDriverPort loadUser, CreateVerificationToken createToken) {
		super();
		this.mapper = mapper;
		this.createUser = createUser;
		this.encoder = encoder;
		this.loadUser = loadUser;
		this.createToken = createToken;
	}



	@Override
	public User create(NewUserDTOIn newUser) throws AppEmailAlreadyRegisteredException{
		Optional<User> oldUser = loadUser.load(newUser.getEmail());
		if(oldUser.isPresent())
			throw new AppEmailAlreadyRegisteredException("There's already an user registered with the email: "+newUser.getEmail());
		
		NewUserData newUserOut = mapper.map(newUser);
		newUserOut.setPassword(encoder.encode(newUserOut.getPassword()));
		User user = createUser.create(newUserOut);
		
		VerificationToken token = createToken.create(newUser.getEmail());
		
		return user;
	}

}
