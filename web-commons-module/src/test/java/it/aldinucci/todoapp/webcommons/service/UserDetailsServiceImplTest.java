package it.aldinucci.todoapp.webcommons.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;

import it.aldinucci.todoapp.application.port.out.LoadUserByEmailDriverPort;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.webcommons.model.UserDetailsImpl;

class UserDetailsServiceImplTest {

	@Mock
	private LoadUserByEmailDriverPort loadUser;
	
	@InjectMocks
	private UserDetailsServiceImpl service;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
	}
	
	@Test
	void test_userDetails() {
		UserDetailsImpl userDetails = new UserDetailsImpl("email", "password");
		User user = new User("email", "username", "password");
		when(loadUser.load("another@mail")).thenReturn(user);
		
		UserDetails loadedDetails = service.loadUserByUsername("another@mail");
		
		assertThat(loadedDetails).isEqualTo(userDetails);
	}

}
