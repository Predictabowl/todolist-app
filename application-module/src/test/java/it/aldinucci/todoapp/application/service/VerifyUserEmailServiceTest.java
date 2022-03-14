package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.isA;
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

import it.aldinucci.todoapp.application.port.in.dto.VerifyTokenDTOIn;
import it.aldinucci.todoapp.application.port.out.DeleteVerificationTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadUserByEmailDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadVerificationTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.UpdateUserDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.UserData;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;

class VerifyUserEmailServiceTest {
	
	private static final String FIXTURE_EMAIL = "user@test.it";

	private static final String FIXTURE_TOKEN_STRING = "token string";

	@Mock
	private LoadUserByEmailDriverPort loadUser;
	
	@Mock
	private LoadVerificationTokenDriverPort loadToken;
	
	@Mock
	private UpdateUserDriverPort updateUser;
	
	@Mock
	private DeleteVerificationTokenDriverPort deleteToken;
	
	@InjectMocks
	private VerifyUserEmailService service;

	private Calendar calendar;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
		calendar = Calendar.getInstance();
	}

	@Test
	void test_verificationWhenTokenNotFound_shouldFail() {
		VerifyTokenDTOIn tokenDto = new VerifyTokenDTOIn(FIXTURE_TOKEN_STRING);
		when(loadToken.load(isA(String.class))).thenReturn(Optional.empty());
		
		boolean verifyResult = service.verify(tokenDto);
		
		assertThat(verifyResult).isFalse();
		verify(loadToken).load(FIXTURE_TOKEN_STRING);
		verifyNoInteractions(updateUser);
		verifyNoInteractions(loadUser);
		verifyNoInteractions(deleteToken);
	}

	@Test
	void test_verificationWhenTokenExpired_shouldFail_andDeleteToken() {
		VerifyTokenDTOIn tokenDto = new VerifyTokenDTOIn(FIXTURE_TOKEN_STRING);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		VerificationToken token = new VerificationToken(FIXTURE_TOKEN_STRING, calendar.getTime(), FIXTURE_EMAIL);
		when(loadToken.load(isA(String.class))).thenReturn(Optional.of(token));
		
		boolean verifyResult = service.verify(tokenDto);
		
		assertThat(verifyResult).isFalse();
		verify(loadToken).load(FIXTURE_TOKEN_STRING);
		verify(deleteToken).delete(FIXTURE_TOKEN_STRING);
		verifyNoInteractions(updateUser);
		verifyNoInteractions(loadUser);
	}
	
	
	@Test
	void test_verificationWhenCannotFindUser_shouldFail() {
		VerifyTokenDTOIn tokenDto = new VerifyTokenDTOIn(FIXTURE_TOKEN_STRING);
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
	}
	
	@Test
	void test_verificationSuccess_shouldDeleteToken_andUpdateUser() {
		VerifyTokenDTOIn tokenDto = new VerifyTokenDTOIn(FIXTURE_TOKEN_STRING);
		calendar.add(Calendar.DAY_OF_MONTH, +1);
		VerificationToken token = new VerificationToken(FIXTURE_TOKEN_STRING, calendar.getTime(), FIXTURE_EMAIL);
		when(loadToken.load(isA(String.class))).thenReturn(Optional.of(token));
		when(loadUser.load(isA(String.class))).thenReturn(
				Optional.of(new User(FIXTURE_EMAIL, "username", "password", false)));
		
		boolean verifyResult = service.verify(tokenDto);
		
		assertThat(verifyResult).isTrue();
		verify(loadToken).load(FIXTURE_TOKEN_STRING);
		verify(deleteToken).delete(FIXTURE_TOKEN_STRING);
		verify(loadUser).load(FIXTURE_EMAIL);
		verify(updateUser).update(new UserData("username", FIXTURE_EMAIL, "password", true));
	}
}
