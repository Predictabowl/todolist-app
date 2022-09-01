package it.aldinucci.todoapp.application.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Calendar;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.DeleteVerificatinTokenByUserDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadUserByIdDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadVerificationTokenByEmailDriverPort;
import it.aldinucci.todoapp.application.service.util.CreateVerificationToken;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exception.AppUserEmailAlreadyVerifiedException;

class GetOrCreateVerificationTokenServiceTest {

	private static final String FIXTURE_USER_EMAIL = "test@email.it";

	@Mock 
	private LoadUserByIdDriverPort loadUser;
	
	@Mock
	private LoadVerificationTokenByEmailDriverPort loadToken;
	
	@Mock
	private DeleteVerificatinTokenByUserDriverPort deleteToken;
	
	@Mock
	private CreateVerificationToken createToken;
	
	@InjectMocks
	private GetOrCreateVerificationTokenService sut;
	
	private Calendar calendar;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
		calendar = Calendar.getInstance();
	}
	
	@Test
	void test_retrieveToken_whenIsMissing_shouldCreateOne() throws AppUserEmailAlreadyVerifiedException {
		VerificationToken token = new VerificationToken("not so random string", calendar.getTime(), "user@test.it");
		UserIdDTO userId = new UserIdDTO(FIXTURE_USER_EMAIL);
		when(createToken.create(anyString())).thenReturn(token);
		when(loadUser.load(isA(String.class))).thenReturn(Optional.of(new User(FIXTURE_USER_EMAIL, "name", "password", false)));
		when(loadToken.load(anyString())).thenReturn(Optional.empty());
		
		Optional<VerificationToken> createdToken = sut.get(userId);
		
		InOrder inOrder = Mockito.inOrder(createToken, loadUser, loadToken);
		inOrder.verify(loadUser).load(FIXTURE_USER_EMAIL);
		inOrder.verify(loadToken).load(FIXTURE_USER_EMAIL);
		inOrder.verify(createToken).create(FIXTURE_USER_EMAIL);
		verifyNoInteractions(deleteToken);
		assertThat(createdToken).containsSame(token);
				
	}
	
	@Test
	void test_retrieveToken_whenUserIsActive_shouldThrow() {
		when(loadUser.load(isA(String.class)))
			.thenReturn(Optional.of(new User(FIXTURE_USER_EMAIL, "name", "password", true)));
		UserIdDTO userId = new UserIdDTO(FIXTURE_USER_EMAIL);
		
		assertThatThrownBy(()-> sut.get(userId))
			.isInstanceOf(AppUserEmailAlreadyVerifiedException.class)
			.hasMessage("Email "+FIXTURE_USER_EMAIL+" is already verified.");
		
		verify(loadUser).load(FIXTURE_USER_EMAIL);
		verifyNoInteractions(loadToken);
		verifyNoInteractions(deleteToken);
		verifyNoInteractions(createToken);
	}

	@Test
	void test_retrieveToken_whenUserMissing() throws AppUserEmailAlreadyVerifiedException {
		when(loadUser.load(isA(String.class))).thenReturn(Optional.empty());
		UserIdDTO userId = new UserIdDTO(FIXTURE_USER_EMAIL);
		
		Optional<VerificationToken> optional = sut.get(userId);
		
		assertThat(optional).isEmpty();
		verify(loadUser).load(FIXTURE_USER_EMAIL);
		verifyNoInteractions(deleteToken);
		verifyNoInteractions(createToken);
		verifyNoInteractions(loadToken);
	}
	
	@Test
	void test_retrieveToken_whenTokenIsNotExpired_shouldReturnIt() throws AppUserEmailAlreadyVerifiedException {
		when(loadUser.load(isA(String.class)))
			.thenReturn(Optional.of(new User(FIXTURE_USER_EMAIL, "name", "password", false)));
		UserIdDTO userId = new UserIdDTO(FIXTURE_USER_EMAIL);
		calendar.add(Calendar.MINUTE, 10);
		VerificationToken token = new VerificationToken("token-code", calendar.getTime(), FIXTURE_USER_EMAIL);
		when(loadToken.load(anyString())).thenReturn(Optional.of(token));
		
		Optional<VerificationToken> loadedToken = sut.get(userId);
		
		assertThat(loadedToken).containsSame(token);
		verify(loadUser).load(FIXTURE_USER_EMAIL);
		verify(loadToken).load(FIXTURE_USER_EMAIL);
		verifyNoInteractions(deleteToken);
		verifyNoInteractions(createToken);
	}
	
	@Test
	void test_retrieveToken_whenTokenIsExpired_shouldDeleteTheOldOne_andCreateNewOne() throws AppUserEmailAlreadyVerifiedException {
		when(loadUser.load(isA(String.class)))
			.thenReturn(Optional.of(new User(FIXTURE_USER_EMAIL, "name", "password", false)));
		UserIdDTO userId = new UserIdDTO(FIXTURE_USER_EMAIL);
		calendar.add(Calendar.MINUTE, -10);
		VerificationToken oldToken = new VerificationToken("token-code", calendar.getTime(), FIXTURE_USER_EMAIL);
		VerificationToken newToken = new VerificationToken("token-code-2", Calendar.getInstance().getTime(), FIXTURE_USER_EMAIL);
		when(loadToken.load(anyString())).thenReturn(Optional.of(oldToken));
		when(createToken.create(anyString())).thenReturn(newToken);
		
		Optional<VerificationToken> loadedToken = sut.get(userId);
		
		assertThat(loadedToken).containsSame(newToken);
		InOrder inOrder = Mockito.inOrder(loadUser,loadToken, deleteToken, createToken);
		inOrder.verify(loadUser).load(FIXTURE_USER_EMAIL);
		inOrder.verify(loadToken).load(FIXTURE_USER_EMAIL);
		inOrder.verify(deleteToken).delete(FIXTURE_USER_EMAIL);
		inOrder.verify(createToken).create(FIXTURE_USER_EMAIL);
	}
}
