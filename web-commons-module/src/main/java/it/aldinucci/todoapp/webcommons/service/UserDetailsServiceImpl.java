package it.aldinucci.todoapp.webcommons.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.out.LoadUserByEmailDriverPort;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.webcommons.config.security.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	private LoadUserByEmailDriverPort loadUserPort;
	
	@Autowired
	public UserDetailsServiceImpl(LoadUserByEmailDriverPort loadUserPort) {
		this.loadUserPort = loadUserPort;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = loadUserPort.load(username);
		return new UserDetailsImpl(user.getEmail(), user.getPassword());
	}

}
