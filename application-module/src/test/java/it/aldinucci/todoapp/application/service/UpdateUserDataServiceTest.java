package it.aldinucci.todoapp.application.service;


import static org.mockito.MockitoAnnotations.openMocks;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import it.aldinucci.todoapp.application.port.in.dto.UserDataDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadUserByEmailDriverPort;
import it.aldinucci.todoapp.application.port.out.UpdateUserDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.UserData;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

class UpdateUserDataServiceTest {

	private static final String FIXTURE_EMAIL = "email@test.it";
	
	@Mock
	private LoadUserByEmailDriverPort loadUser;
	
	@Mock
	private UpdateUserDriverPort updateUser;
	
	@Mock
	private AppGenericMapper<User, UserData> mapper;
	
	private UpdateUserDataService sut;

	@BeforeEach
	void setUp() {
		openMocks(this);
		sut = new UpdateUserDataService(loadUser, updateUser, mapper);
	}
	
	@Test
	void test_whenUserNotFound() {
		when(loadUser.load(anyString())).thenReturn(Optional.empty());
		
		Optional<User> update = sut.update(new UserIdDTO(FIXTURE_EMAIL), new UserDataDTOIn("new name"));
		
		assertThat(update).isEmpty();
		verify(loadUser).load(FIXTURE_EMAIL);
		verifyNoInteractions(updateUser);
		verifyNoInteractions(mapper);
	}
	
	@Test
	void test_updateSuccessful() {
		User user = new User(FIXTURE_EMAIL, "old name", "pass2", true);
		User newUser = new User();
		UserData userData = new UserData("new name", FIXTURE_EMAIL, "pass2", true);
		when(loadUser.load(anyString())).thenReturn(Optional.of(user));
		when(updateUser.update(any())).thenReturn(newUser);
		when(mapper.map(any())).thenReturn(userData);
		
		Optional<User> update = sut.update(new UserIdDTO(FIXTURE_EMAIL), new UserDataDTOIn("new name"));
		
		assertThat(update).containsSame(newUser);
		verify(loadUser).load(FIXTURE_EMAIL);
		verify(updateUser).update(userData);
		verify(mapper).map(new User(FIXTURE_EMAIL, "new name", "pass2", true));
	}

}
