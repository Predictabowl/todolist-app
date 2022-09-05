package it.aldinucci.todoapp.application.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.LoadUserByIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadUserByIdDriverPort;
import it.aldinucci.todoapp.domain.User;

@Service
@Transactional
public class LoadUserByIdService implements LoadUserByIdUsePort{

	private final LoadUserByIdDriverPort loadUserPort;
	
	
	@Autowired
	public LoadUserByIdService(LoadUserByIdDriverPort loadUserPort) {
		super();
		this.loadUserPort = loadUserPort;
	}


	@Override
	public Optional<User> load(UserIdDTO id){
		return loadUserPort.load(id.getId());
	}

}
