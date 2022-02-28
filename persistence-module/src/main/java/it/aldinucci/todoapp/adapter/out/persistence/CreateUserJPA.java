package it.aldinucci.todoapp.adapter.out.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;
import it.aldinucci.todoapp.application.port.out.CreateUserDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.NewUserDTOOut;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class CreateUserJPA implements CreateUserDriverPort{

	private UserJPARepository userRepo;
	private AppGenericMapper<NewUserDTOOut, UserJPA> userJPAMapper;
	private AppGenericMapper<UserJPA, User> userMapper;
	
	@Autowired
	public CreateUserJPA(UserJPARepository userRepo, AppGenericMapper<NewUserDTOOut, UserJPA> userJPAMapper,
			AppGenericMapper<UserJPA, User> userMapper) {
		this.userRepo = userRepo;
		this.userJPAMapper = userJPAMapper;
		this.userMapper = userMapper;
	}


	@Override
	public User create(NewUserDTOOut newUser) {
		return userMapper.map(userRepo.save(userJPAMapper.map(newUser)));
	}

}
