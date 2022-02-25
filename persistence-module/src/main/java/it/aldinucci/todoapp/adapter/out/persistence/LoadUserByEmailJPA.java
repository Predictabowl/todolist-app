package it.aldinucci.todoapp.adapter.out.persistence;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;
import it.aldinucci.todoapp.application.port.out.LoadUserByEmailDriverPort;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exceptions.AppUserNotFoundException;

@Component
@Transactional
public class LoadUserByEmailJPA implements LoadUserByEmailDriverPort{

	private ModelMapper mapper;
	private UserJPARepository userRepo;
	
	@Autowired
	public LoadUserByEmailJPA(ModelMapper mapper, UserJPARepository userRepo) {
		this.mapper = mapper;
		this.userRepo = userRepo;
	}


	@Override
	public User load(String email) {
		return mapper.map(userRepo.findByEmail(email).orElseThrow(() 
				-> new AppUserNotFoundException("User not found with email: "+email))
				, User.class);
	}

}
