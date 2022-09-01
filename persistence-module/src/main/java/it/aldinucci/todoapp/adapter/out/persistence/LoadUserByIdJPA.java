package it.aldinucci.todoapp.adapter.out.persistence;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;
import it.aldinucci.todoapp.application.port.out.LoadUserByIdDriverPort;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
@Transactional
public class LoadUserByIdJPA implements LoadUserByIdDriverPort{

	private AppGenericMapper<UserJPA, User> mapper;
	private UserJPARepository userRepo;
	
	@Autowired
	public LoadUserByIdJPA(AppGenericMapper<UserJPA, User> mapper, UserJPARepository userRepo) {
		super();
		this.mapper = mapper;
		this.userRepo = userRepo;
	}

	@Override
	public Optional<User> load(String email) {
		Optional<UserJPA> optionalUser = userRepo.findByEmail(email);
		if(optionalUser.isEmpty())
				return Optional.empty();
		return Optional.of(mapper.map(optionalUser.get()));	}

}
