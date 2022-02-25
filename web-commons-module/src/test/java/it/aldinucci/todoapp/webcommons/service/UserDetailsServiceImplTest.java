package it.aldinucci.todoapp.webcommons.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;

import it.aldinucci.todoapp.application.port.in.LoadUserByEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.webcommons.model.UserDetailsImpl;

class UserDetailsServiceImplTest {

	@Mock
	private LoadUserByEmailUsePort loadUser;
	
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
		when(loadUser.load(isA(UserIdDTO.class))).thenReturn(user);
		
		UserDetails loadedDetails = service.loadUserByUsername("another@mail");
		
		assertThat(loadedDetails).isEqualTo(userDetails);
		verify(loadUser).load(new UserIdDTO("another@mail"));
	}

}
