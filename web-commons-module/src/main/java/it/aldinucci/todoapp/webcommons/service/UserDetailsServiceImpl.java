package it.aldinucci.todoapp.webcommons.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.aldinucci.todoapp.application.port.in.LoadUserByIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.webcommons.model.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private LoadUserByIdUsePort loadUserPort;

	public UserDetailsServiceImpl(LoadUserByIdUsePort loadUserPort) {
		this.loadUserPort = loadUserPort;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = loadUserPort.load(new UserIdDTO(username))
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
		return new UserDetailsImpl(user.getEmail(), user.getPassword(), user.isEnabled());
	}

}
