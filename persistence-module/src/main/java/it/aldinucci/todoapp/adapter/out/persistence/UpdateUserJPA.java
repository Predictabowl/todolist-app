package it.aldinucci.todoapp.adapter.out.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;
import it.aldinucci.todoapp.application.port.out.UpdateUserDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.UserData;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@Component
public class UpdateUserJPA implements UpdateUserDriverPort {

	private UserJPARepository userRepo;
	private AppGenericMapper<UserJPA, User> mapper;

	@Autowired
	public UpdateUserJPA(UserJPARepository userRepo, AppGenericMapper<UserJPA, User> mapper) {
		this.userRepo = userRepo;
		this.mapper = mapper;
	}

	@Override
	public User update(UserData user) {
		UserJPA userJPA = userRepo.findByEmail(user.getEmail())
				.orElseThrow(() -> new AppUserNotFoundException("Could not find user with email: " + user.getEmail()));
		userJPA.setPassword(user.getPassword());
		userJPA.setUsername(user.getUsername());
		userJPA.setEnabled(user.isEnabled());
		return mapper.map(userRepo.save(userJPA));
	}

}
