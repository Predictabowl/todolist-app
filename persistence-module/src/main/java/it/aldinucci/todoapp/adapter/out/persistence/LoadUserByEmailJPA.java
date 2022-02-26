package it.aldinucci.todoapp.adapter.out.persistence;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;
import it.aldinucci.todoapp.application.port.out.LoadUserByEmailDriverPort;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exceptions.AppUserNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
@Transactional
public class LoadUserByEmailJPA implements LoadUserByEmailDriverPort{

	private AppGenericMapper<UserJPA, User> mapper;
	private UserJPARepository userRepo;
	
	@Autowired
	public LoadUserByEmailJPA(AppGenericMapper<UserJPA, User> mapper, UserJPARepository userRepo) {
		super();
		this.mapper = mapper;
		this.userRepo = userRepo;
	}

	@Override
	public User load(String email) {
		return mapper.map(userRepo.findByEmail(email).orElseThrow(() 
				-> new AppUserNotFoundException("User not found with email: "+email)));
	}

}
