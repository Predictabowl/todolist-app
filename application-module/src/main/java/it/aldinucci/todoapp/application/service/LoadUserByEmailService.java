package it.aldinucci.todoapp.application.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.LoadUserByEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadUserByEmailDriverPort;
import it.aldinucci.todoapp.domain.User;

@Service
@Transactional
public class LoadUserByEmailService implements LoadUserByEmailUsePort{

	private LoadUserByEmailDriverPort loadUserPort;
	
	
	@Autowired
	public LoadUserByEmailService(LoadUserByEmailDriverPort loadUserPort) {
		super();
		this.loadUserPort = loadUserPort;
	}


	@Override
	public Optional<User> load(UserIdDTO id){
		return loadUserPort.load(id.getEmail());
	}

}
