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
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.DeleteVerificatinTokenByUserDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadUserByEmailDriverPort;
import it.aldinucci.todoapp.application.service.util.CreateVerificationToken;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exceptions.AppUserNotFoundException;

class CreateOrReplaceVerificationTokenServiceTest {

	private static final String FIXTURE_USER_EMAIL = "user@email.it";

	@Mock 
	private LoadUserByEmailDriverPort loadUser;
	
	@Mock
	private DeleteVerificatinTokenByUserDriverPort deleteToken;
	
	@Mock
	private CreateVerificationToken createToken;
	
	@InjectMocks
	private CreateOrReplaceVerificationTokenService tokenService;
	
	private Date date;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
		date = Calendar.getInstance().getTime();
	}
	
	@Test
	void test_createTokenUsingProperties_success() {
		VerificationToken token = new VerificationToken("not so random string", date, "user@test.it");
		when(createToken.create(anyString())).thenReturn(token);
		when(loadUser.load(isA(String.class))).thenReturn(Optional.of(new User(FIXTURE_USER_EMAIL, "name", "password", false)));
		
		VerificationToken createdToken = tokenService.create(new UserIdDTO(FIXTURE_USER_EMAIL));
		
		InOrder inOrder = Mockito.inOrder(createToken, loadUser, deleteToken);
		inOrder.verify(loadUser).load(FIXTURE_USER_EMAIL);
		inOrder.verify(deleteToken).delete(FIXTURE_USER_EMAIL);
		inOrder.verify(createToken).create(FIXTURE_USER_EMAIL);
		assertThat(createdToken).isSameAs(token);
				
	}
	
	@Test
	void test_createTokenWhenUserIsActive_shouldReturnNull() {
		when(loadUser.load(isA(String.class))).thenReturn(Optional.of(new User(FIXTURE_USER_EMAIL, "name", "password", true)));
		
		VerificationToken createdToken = tokenService.create(new UserIdDTO(FIXTURE_USER_EMAIL));
		
		assertThat(createdToken).isNull();
		verify(loadUser).load(FIXTURE_USER_EMAIL);
		verifyNoInteractions(deleteToken);
		verifyNoInteractions(createToken);
	}

	@Test
	void test_createToken_whenUserMissing_shouldThrow() {
		when(loadUser.load(isA(String.class))).thenReturn(Optional.empty());
		UserIdDTO userId = new UserIdDTO(FIXTURE_USER_EMAIL);
		
		assertThatThrownBy(() -> tokenService.create(userId))
			.isInstanceOf(AppUserNotFoundException.class)
			.hasMessage("Could not find user with email: "+FIXTURE_USER_EMAIL);
		
		verify(loadUser).load(FIXTURE_USER_EMAIL);
		verifyNoInteractions(deleteToken);
		verifyNoInteractions(createToken);
	}
}
