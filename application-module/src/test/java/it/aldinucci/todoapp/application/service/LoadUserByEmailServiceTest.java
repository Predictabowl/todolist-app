package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadUserByEmailDriverPort;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exceptions.AppUserNotFoundException;

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
	void test_loadUser_whenPresent() throws AppUserNotFoundException {
		User user = new User("new@mail.it", "username", "password");
		when(driverPort.load(anyString())).thenReturn(Optional.of(user));
		
		User loadedUser = loadService.load(new UserIdDTO("test@email.it"));
		
		verify(driverPort).load("test@email.it");
		assertThat(loadedUser).isSameAs(user);
	}
	
	@Test
	void test_loadUser_WhenNotPresent_shouldThrow() {
		when(driverPort.load(anyString())).thenReturn(Optional.empty());
		UserIdDTO userIdDTO = new UserIdDTO("test@email.it");
		
		assertThatThrownBy(() -> loadService.load(userIdDTO))
			.isInstanceOf(AppUserNotFoundException.class)
			.hasMessage("User not found with email: test@email.it");
	}

}
