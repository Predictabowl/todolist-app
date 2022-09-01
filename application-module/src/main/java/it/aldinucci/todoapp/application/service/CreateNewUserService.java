package it.aldinucci.todoapp.application.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.CreateUserUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewUserDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.NewUserDtoOut;
import it.aldinucci.todoapp.application.port.out.CreateUserDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadUserByIdDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.NewUserData;
import it.aldinucci.todoapp.application.service.util.CreateVerificationToken;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exception.AppEmailAlreadyRegisteredException;
import it.aldinucci.todoapp.util.AppPasswordEncoder;

@Service
@Transactional
public class CreateNewUserService implements CreateUserUsePort {

	private CreateUserDriverPort createUser;
	private AppPasswordEncoder encoder;
	private LoadUserByIdDriverPort loadUser;
	private CreateVerificationToken createToken;

	@Autowired
	public CreateNewUserService(CreateUserDriverPort createUser, AppPasswordEncoder encoder,
			LoadUserByIdDriverPort loadUser, CreateVerificationToken createToken) {
		super();
		this.createUser = createUser;
		this.encoder = encoder;
		this.loadUser = loadUser;
		this.createToken = createToken;
	}

	@Override
	public NewUserDtoOut create(NewUserDTOIn newUser) throws AppEmailAlreadyRegisteredException {
		if (loadUser.load(newUser.getEmail()).isPresent())
			throw new AppEmailAlreadyRegisteredException(
					"There's already an user registered with the email: " + newUser.getEmail());

		User user = createUser.create(
				new NewUserData(newUser.getUsername(), newUser.getEmail(), encoder.encode(newUser.getPassword())));

		VerificationToken token = createToken.create(newUser.getEmail());

		return new NewUserDtoOut(user, token);
	}

}
