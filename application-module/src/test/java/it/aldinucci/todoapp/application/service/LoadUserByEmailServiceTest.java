package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Calendar;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.DeleteUserByEmailDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadUserByEmailDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadVerificationTokenByEmailDriverPort;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exceptions.AppUserNotFoundException;

class LoadUserByEmailServiceTest {

	private static final String FIXTURE_EMAIL = "test@email.it";

	@Mock
	private LoadUserByEmailDriverPort loadUser;
	
	@Mock
	private LoadVerificationTokenByEmailDriverPort loadToken;
	
	@Mock
	private DeleteUserByEmailDriverPort deleteUser;
	
	@InjectMocks
	private LoadUserByEmailService sut;

	private Calendar calendar;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
		calendar = Calendar.getInstance();
	}
	
	@Test
	void test_loadUser_success() throws AppUserNotFoundException {
		User user = new User("new@mail.it", "username", "password",true);
		when(loadUser.load(anyString())).thenReturn(Optional.of(user));
		
		Optional<User> loadedUser = sut.load(new UserIdDTO(FIXTURE_EMAIL));
		
		assertThat(loadedUser).containsSame(user);
		verify(loadUser).load(FIXTURE_EMAIL);
		verifyNoInteractions(loadToken);
		verifyNoInteractions(deleteUser);
	}

	@Test
	void test_loadUser_whenNotEnabled_butWithNoVerificationToken_success() throws AppUserNotFoundException {
		User user = new User("new@mail.it", "username", "password",false);
		when(loadUser.load(anyString())).thenReturn(Optional.of(user));
		calendar.add(Calendar.MINUTE, 10);
		when(loadToken.load(anyString())).thenReturn(
				Optional.of(new VerificationToken("code", calendar.getTime(), "new@mail.it")));
		
		Optional<User> loadedUser = sut.load(new UserIdDTO(FIXTURE_EMAIL));
		
		assertThat(loadedUser).containsSame(user);
		verify(loadUser).load(FIXTURE_EMAIL);
		verify(loadToken).load(FIXTURE_EMAIL);
		verifyNoInteractions(deleteUser);
	}

	
	@Test
	void test_loadUser_whenNotPresent() {
		when(loadUser.load(anyString())).thenReturn(Optional.empty());
		
		Optional<User> loadedUser = sut.load(new UserIdDTO(FIXTURE_EMAIL));
		
		assertThat(loadedUser).isEmpty();
		verify(loadUser).load(FIXTURE_EMAIL);
		verifyNoInteractions(loadToken);
		verifyNoInteractions(deleteUser);
	}
	
	@Test
	void test_loadUser_whenNotEnabled_andWithNoVerificationToken_shouldDeleteIt() throws AppUserNotFoundException {
		User user = new User("new@mail.it", "username", "password",false);
		when(loadUser.load(anyString())).thenReturn(Optional.of(user));
		when(loadToken.load(anyString())).thenReturn(Optional.empty());
		
		Optional<User> loadedUser = sut.load(new UserIdDTO(FIXTURE_EMAIL));
		
		assertThat(loadedUser).isEmpty();
		verify(loadUser).load(FIXTURE_EMAIL);
		verify(loadToken).load(FIXTURE_EMAIL);
		verify(deleteUser).delete(FIXTURE_EMAIL);
	}
	
	@Test
	void test_loadUser_whenNotEnabled_andExpiredToken_shouldDeleteIt() throws AppUserNotFoundException {
		User user = new User("new@mail.it", "username", "password",false);
		when(loadUser.load(anyString())).thenReturn(Optional.of(user));
		calendar.add(Calendar.MINUTE, -10);
		when(loadToken.load(anyString())).thenReturn(
				Optional.of(new VerificationToken("code", calendar.getTime(), "new@mail.it")));
		
		Optional<User> loadedUser = sut.load(new UserIdDTO(FIXTURE_EMAIL));
		
		assertThat(loadedUser).isEmpty();
		verify(loadUser).load(FIXTURE_EMAIL);
		verify(loadToken).load(FIXTURE_EMAIL);
		verify(deleteUser).delete(FIXTURE_EMAIL);
	}
	

}
