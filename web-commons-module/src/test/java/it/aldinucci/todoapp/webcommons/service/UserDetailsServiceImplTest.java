package it.aldinucci.todoapp.webcommons.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import it.aldinucci.todoapp.application.port.in.LoadUserByIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.webcommons.model.UserDetailsImpl;

class UserDetailsServiceImplTest {

	@Mock
	private LoadUserByIdUsePort loadUser;
	
	@InjectMocks
	private UserDetailsServiceImpl service;

	private AutoCloseable closeable;
	
	@BeforeEach
	void setUp() {
		closeable = openMocks(this);
	}
	
	@AfterEach
	void tearDown() throws Exception {
		closeable.close();
	}
	
	@Test
	void test_userDetails_success(){
		User user = new User("email", "username", "password");
		UserDetailsImpl userDetails = new UserDetailsImpl("email","password",false);
		when(loadUser.load(isA(UserIdDTO.class))).thenReturn(Optional.of(user));
		
		UserDetails loadedDetails = service.loadUserByUsername("another@mail");
		
		assertThat(loadedDetails).isEqualTo(userDetails);
		verify(loadUser).load(new UserIdDTO("another@mail"));
	}
	
	@Test
	void test_userDetails_whenUSerNotFound_shouldThrow(){
		when(loadUser.load(isA(UserIdDTO.class))).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> service.loadUserByUsername("another@mail"))
			.isInstanceOf(UsernameNotFoundException.class)
			.hasMessage("User not found with email: another@mail");
		
		verify(loadUser).load(new UserIdDTO("another@mail"));
	}

}
