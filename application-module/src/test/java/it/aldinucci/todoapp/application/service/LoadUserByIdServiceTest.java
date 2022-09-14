package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadUserByIdDriverPort;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;

class LoadUserByIdServiceTest {

	private static final String FIXTURE_EMAIL = "test@email.it";

	@Mock
	private LoadUserByIdDriverPort loadUser;
	
	@InjectMocks
	private LoadUserByIdService sut;

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
	void test_loadUser_success() throws AppUserNotFoundException {
		User user = new User("new@mail.it", "username", "password",true);
		when(loadUser.load(anyString())).thenReturn(Optional.of(user));
		
		Optional<User> loadedUser = sut.load(new UserIdDTO(FIXTURE_EMAIL));
		
		assertThat(loadedUser).containsSame(user);
	}
	
	@Test
	void test_loadUser_whenNotPresent() {
		when(loadUser.load(anyString())).thenReturn(Optional.empty());
		
		Optional<User> loadedUser = sut.load(new UserIdDTO(FIXTURE_EMAIL));
		
		assertThat(loadedUser).isEmpty();
	}
	
}
