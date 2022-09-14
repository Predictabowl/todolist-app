package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Calendar;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import it.aldinucci.todoapp.application.port.in.dto.StringTokenDTOIn;
import it.aldinucci.todoapp.application.port.out.DeleteVerificationTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadUserByIdDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadVerificationTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.UpdateUserDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.UserData;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

class VerifyUserEmailServiceTest {
	
	private static final String FIXTURE_EMAIL = "user@test.it";

	private static final String FIXTURE_TOKEN_STRING = "token string";

	@Mock
	private LoadUserByIdDriverPort loadUser;
	
	@Mock
	private LoadVerificationTokenDriverPort loadToken;
	
	@Mock
	private UpdateUserDriverPort updateUser;
	
	@Mock
	private DeleteVerificationTokenDriverPort deleteToken;
	
	@Mock
	private AppGenericMapper<User, UserData> mapper;
	
	private VerifyUserEmailService service;

	private Calendar calendar;

	private AutoCloseable closeable;
	
	@BeforeEach
	void setUp() {
		closeable = openMocks(this);
		calendar = Calendar.getInstance();
		service = new VerifyUserEmailService(loadToken, loadUser, updateUser, deleteToken, mapper);
	}
	
	@AfterEach
	void tearDown() throws Exception {
		closeable.close();
	}

	@Test
	void test_verificationWhenTokenNotFound_shouldFail() {
		StringTokenDTOIn tokenDto = new StringTokenDTOIn(FIXTURE_TOKEN_STRING);
		when(loadToken.load(isA(String.class))).thenReturn(Optional.empty());
		
		boolean verifyResult = service.verify(tokenDto);
		
		assertThat(verifyResult).isFalse();
		verify(loadToken).load(FIXTURE_TOKEN_STRING);
		verifyNoInteractions(updateUser);
		verifyNoInteractions(loadUser);
		verifyNoInteractions(deleteToken);
		verifyNoInteractions(mapper);
	}

	@Test
	void test_verificationWhenTokenExpired_shouldFail_andDeleteToken() {
		StringTokenDTOIn tokenDto = new StringTokenDTOIn(FIXTURE_TOKEN_STRING);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		VerificationToken token = new VerificationToken(FIXTURE_TOKEN_STRING, calendar.getTime(), FIXTURE_EMAIL);
		when(loadToken.load(isA(String.class))).thenReturn(Optional.of(token));
		
		boolean verifyResult = service.verify(tokenDto);
		
		assertThat(verifyResult).isFalse();
		verify(loadToken).load(FIXTURE_TOKEN_STRING);
		verify(deleteToken).delete(FIXTURE_TOKEN_STRING);
		verifyNoInteractions(updateUser);
		verifyNoInteractions(loadUser);
		verifyNoInteractions(mapper);
	}
	
	
	@Test
	void test_verificationWhenCannotFindUser_shouldFail() {
		StringTokenDTOIn tokenDto = new StringTokenDTOIn(FIXTURE_TOKEN_STRING);
		calendar.add(Calendar.DAY_OF_MONTH, +1);
		VerificationToken token = new VerificationToken(FIXTURE_TOKEN_STRING, calendar.getTime(), FIXTURE_EMAIL);
		when(loadToken.load(isA(String.class))).thenReturn(Optional.of(token));
		when(loadUser.load(isA(String.class))).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> service.verify(tokenDto))
			.isExactlyInstanceOf(AppUserNotFoundException.class)
			.hasMessage("Data Integrity compromised, could not find user linked with token with email: "+FIXTURE_EMAIL);
		
		verify(loadToken).load(FIXTURE_TOKEN_STRING);
		verify(deleteToken).delete(FIXTURE_TOKEN_STRING);
		verify(loadUser).load(FIXTURE_EMAIL);
		verifyNoInteractions(updateUser);
		verifyNoInteractions(mapper);
	}
	
	@Test
	void test_verificationSuccess_shouldDeleteToken_andUpdateUser() {
		StringTokenDTOIn tokenDto = new StringTokenDTOIn(FIXTURE_TOKEN_STRING);
		UserData userData = new UserData("a name", FIXTURE_EMAIL, "a password", false);
		calendar.add(Calendar.DAY_OF_MONTH, +1);
		VerificationToken token = new VerificationToken(FIXTURE_TOKEN_STRING, calendar.getTime(), FIXTURE_EMAIL);
		when(loadToken.load(isA(String.class))).thenReturn(Optional.of(token));
		when(loadUser.load(isA(String.class))).thenReturn(
				Optional.of(new User(FIXTURE_EMAIL, "username", "password", false)));
		when(mapper.map(any())).thenReturn(userData);
		
		boolean verifyResult = service.verify(tokenDto);
		
		assertThat(verifyResult).isTrue();
		verify(loadToken).load(FIXTURE_TOKEN_STRING);
		verify(deleteToken).delete(FIXTURE_TOKEN_STRING);
		verify(loadUser).load(FIXTURE_EMAIL);
		verify(mapper).map(new User(FIXTURE_EMAIL, "username", "password", true));
		verify(updateUser).update(userData);
	}
}
