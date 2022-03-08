package it.aldinucci.todoapp.webcommons.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.LoadUserByEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.webcommons.model.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	private LoadUserByEmailUsePort loadUserPort;
	
	@Autowired
	public UserDetailsServiceImpl(LoadUserByEmailUsePort loadUserPort) {
		this.loadUserPort = loadUserPort;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return new UserDetailsImpl(loadUserPort.load(new UserIdDTO(username))
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username)));
	}

}
