package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import it.aldinucci.todoapp.application.port.in.model.AppPassword;
import it.aldinucci.todoapp.application.port.out.DeleteRestPasswordTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadResetPasswordTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadUserByIdDriverPort;
import it.aldinucci.todoapp.application.port.out.UpdateUserDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.UserData;
import it.aldinucci.todoapp.domain.ResetPasswordToken;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;
import it.aldinucci.todoapp.util.AppPasswordEncoder;

class ChangeUserPasswordServiceTest {

	private static final String FIXTURE_EMAIL = "email@test.it";
	private static final String FIXTURE_TOKEN = "token-code";
	private static final String FIXTURE_PASSWORD = "newSimplePassword";

	@Mock
	private LoadResetPasswordTokenDriverPort loadToken;

	@Mock
	private LoadUserByIdDriverPort loadUser;

	@Mock
	private UpdateUserDriverPort updateUser;

	@Mock
	private DeleteRestPasswordTokenDriverPort deleteToken;
	
	@Mock
	private AppPasswordEncoder encoder;
	
	@Mock
	private AppGenericMapper<User, UserData> mapper;

	private ChangeUserPasswordService sut;

	private StringTokenDTOIn tokenDto;

	private AppPassword passwordDto;
	
	private Calendar calendar;
	private AutoCloseable closeable;

	@BeforeEach
	void setUp() {
		closeable = openMocks(this);
		sut = new ChangeUserPasswordService(loadToken, loadUser, updateUser, deleteToken, encoder, mapper);
		tokenDto = new StringTokenDTOIn(FIXTURE_TOKEN);
		passwordDto = new AppPassword(FIXTURE_PASSWORD);
		calendar = Calendar.getInstance();
	}
	
	@AfterEach
	void tearDown() throws Exception {
		closeable.close();
	}

	@Test
	void test_changePassword_whenTokenIsMissing() {
		when(loadToken.load(anyString())).thenReturn(Optional.empty());

		boolean result = sut.change(tokenDto, passwordDto);

		assertThat(result).isFalse();
		verify(loadToken).load(FIXTURE_TOKEN);
		verifyNoInteractions(updateUser);
		verifyNoInteractions(loadUser);
		verifyNoInteractions(deleteToken);
		verifyNoInteractions(encoder);
		verifyNoInteractions(mapper);
	}

	@Test
	void test_changePassword_whenTokenIsExpired() {
		calendar.add(Calendar.MINUTE, -10);
		ResetPasswordToken token = new ResetPasswordToken(FIXTURE_TOKEN, calendar.getTime(), FIXTURE_EMAIL);
		when(loadToken.load(anyString())).thenReturn(Optional.of(token));
		
		boolean result = sut.change(tokenDto, passwordDto);

		assertThat(result).isFalse();
		verify(loadToken).load(FIXTURE_TOKEN);
		verify(deleteToken).delete(FIXTURE_TOKEN);
		verifyNoInteractions(updateUser);
		verifyNoInteractions(loadUser);
		verifyNoInteractions(encoder);
		verifyNoInteractions(mapper);
	}
	
	
	/**
	 * This is an exceptional case, should never happen unless 
	 * something is wrong or the database or the app crashes 
	 */
	@Test
	void test_changePassword_whenCannotFindUser() {
		calendar.add(Calendar.MINUTE, 10);
		ResetPasswordToken token = new ResetPasswordToken(FIXTURE_TOKEN, calendar.getTime(), FIXTURE_EMAIL);
		when(loadToken.load(anyString())).thenReturn(Optional.of(token));
		when(loadUser.load(FIXTURE_EMAIL)).thenReturn(Optional.empty());
		
		assertThatThrownBy(() -> sut.change(tokenDto, passwordDto))
			.isInstanceOf(AppUserNotFoundException.class)
			.hasMessage("Could not find user with email: "+FIXTURE_EMAIL);

		verify(loadToken).load(FIXTURE_TOKEN);
		verify(deleteToken).delete(FIXTURE_TOKEN);
		verify(loadUser).load(FIXTURE_EMAIL);
		verifyNoInteractions(updateUser);
		verifyNoInteractions(encoder);
		verifyNoInteractions(mapper);
	}

	@Test
	void test_changePassword_whenTokenIsValid() {
		calendar.add(Calendar.MINUTE, 10);
		User user = new User(FIXTURE_EMAIL, "username", "old password", true);
		UserData userData = new UserData("a name", "different@email.it", "different password", true);
		ResetPasswordToken token = new ResetPasswordToken(FIXTURE_TOKEN, calendar.getTime(), FIXTURE_EMAIL);
		when(loadToken.load(anyString())).thenReturn(Optional.of(token));
		when(loadUser.load(FIXTURE_EMAIL)).thenReturn(Optional.of(user));
		when(encoder.encode(anyString())).thenReturn("encoded new password");
		when(mapper.map(any())).thenReturn(userData);
		
		boolean result = sut.change(tokenDto, passwordDto);

		assertThat(result).isTrue();
		verify(loadToken).load(FIXTURE_TOKEN);
		verify(deleteToken).delete(FIXTURE_TOKEN);
		verify(loadUser).load(FIXTURE_EMAIL);
		verify(encoder).encode(FIXTURE_PASSWORD);
		verify(mapper).map(new User(FIXTURE_EMAIL, "username", "encoded new password", true));
		verify(updateUser).update(userData);
	}

}
