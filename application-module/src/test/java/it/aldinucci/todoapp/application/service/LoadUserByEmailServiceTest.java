package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadUserByEmailDriverPort;
import it.aldinucci.todoapp.domain.User;

class LoadUserByEmailServiceTest {

	@Mock
	private LoadUserByEmailDriverPort driverPort;
	
	@InjectMocks
	private LoadUserByEmailService loadService;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
	}
	
	@Test
	void test_loadUser_successful() {
		User user = new User("new@mail.it", "username", "password");
		when(driverPort.load(anyString())).thenReturn(user);
		
		User loadedUser = loadService.load(new UserIdDTO("test@email.it"));
		
		verify(driverPort).load("test@email.it");
		assertThat(loadedUser).isSameAs(user);
	}

}
