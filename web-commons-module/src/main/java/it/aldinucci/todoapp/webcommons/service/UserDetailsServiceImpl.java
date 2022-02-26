package it.aldinucci.todoapp.webcommons.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.LoadUserByEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.webcommons.model.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	private LoadUserByEmailUsePort loadUserPort;
	
	@Autowired
	public UserDetailsServiceImpl(LoadUserByEmailUsePort loadUserPort) {
		super();
		this.loadUserPort = loadUserPort;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = loadUserPort.load(new UserIdDTO(username));
		return new UserDetailsImpl(user.getEmail(), user.getPassword());
	}

}
