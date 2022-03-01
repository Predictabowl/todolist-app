package it.aldinucci.todoapp.application.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.LoadUserByEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadUserByEmailDriverPort;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exceptions.AppUserNotFoundException;

@Service
@Transactional
public class LoadUserByEmailService implements LoadUserByEmailUsePort{

	private LoadUserByEmailDriverPort loadUserPort;
	
	@Autowired
	public LoadUserByEmailService(LoadUserByEmailDriverPort loadUserPort) {
		this.loadUserPort = loadUserPort;
	}

	@Override
	public User load(UserIdDTO id) throws AppUserNotFoundException{
		return loadUserPort.load(id.getEmail()).orElseThrow(() 
				-> new AppUserNotFoundException("User not found with email: "+id.getEmail()));

	}

}
