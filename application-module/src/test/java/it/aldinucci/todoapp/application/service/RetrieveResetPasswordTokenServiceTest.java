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
import org.mockito.Mock;

import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.DeleteRestPasswordTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadResetPasswordTokenByEmailDriverPort;
import it.aldinucci.todoapp.application.service.util.CreateResetPasswordToken;
import it.aldinucci.todoapp.domain.ResetPasswordToken;

class RetrieveResetPasswordTokenServiceTest {

	private static final String FIXTURE_EMAIL = "test@email.it"; 
	
	@Mock
	private CreateResetPasswordToken createToken;
	
	@Mock
	private DeleteRestPasswordTokenDriverPort deleteToken;
	
	@Mock
	private LoadResetPasswordTokenByEmailDriverPort loadToken;
	
	private RetrieveResetPasswordTokenService sut;
	
	private Calendar calendar;
	
	/*
	 * Note:
	 * Is preferable to use the constructor itself instead of the annotation InjectMock,
	 * the reason being the InjectMock will work even if the constructor is not 
	 * implemented correctly, so manually instancing the object will implicitly
	 * test the constructor.
	 */
	@BeforeEach
	void setUp() {
		openMocks(this); 
		sut = new RetrieveResetPasswordTokenService(loadToken, deleteToken, createToken);
		calendar = Calendar.getInstance();
	}
	
	@Test
	void test_getWhenValidTokenAlreadyExists() {
		calendar.add(Calendar.HOUR, 1);
		ResetPasswordToken token = new ResetPasswordToken("code", calendar.getTime(), FIXTURE_EMAIL);
		when(loadToken.load(FIXTURE_EMAIL)).thenReturn(Optional.of(token));
		
		ResetPasswordToken createdToken = sut.get(new UserIdDTO(FIXTURE_EMAIL));
		
		assertThat(createdToken).isSameAs(token);
		verify(loadToken).load(FIXTURE_EMAIL);
		verifyNoInteractions(createToken);
		verifyNoInteractions(deleteToken);
	}
	
	@Test
	void test_getWhenTokenNotExists() {
		calendar.add(Calendar.HOUR, 1);
		ResetPasswordToken token = new ResetPasswordToken("code", calendar.getTime(), FIXTURE_EMAIL);
		when(loadToken.load(FIXTURE_EMAIL)).thenReturn(Optional.empty());
		when(createToken.create(anyString())).thenReturn(token);
		UserIdDTO userIdDTO = new UserIdDTO(FIXTURE_EMAIL);
		
		ResetPasswordToken createdToken = sut.get(userIdDTO);
		
		assertThat(createdToken).isSameAs(token);
		verify(loadToken).load(FIXTURE_EMAIL);
		verify(createToken).create(FIXTURE_EMAIL);
		verifyNoInteractions(deleteToken);
	}
	
	@Test
	void test_getWhenTokenExists_butIsExpired() {
		calendar.add(Calendar.HOUR, -1);
		ResetPasswordToken oldToken = new ResetPasswordToken("old code", calendar.getTime(), FIXTURE_EMAIL);
		ResetPasswordToken token = new ResetPasswordToken("new code", Calendar.getInstance().getTime(), FIXTURE_EMAIL);
		when(loadToken.load(FIXTURE_EMAIL)).thenReturn(Optional.of(oldToken));
		when(createToken.create(anyString())).thenReturn(token);
		UserIdDTO userIdDTO = new UserIdDTO(FIXTURE_EMAIL);
		
		ResetPasswordToken createdToken = sut.get(userIdDTO);
		
		assertThat(createdToken).isSameAs(token);
		verify(loadToken).load(FIXTURE_EMAIL);
		verify(deleteToken).delete("old code");
		verify(createToken).create(FIXTURE_EMAIL);
	}
	
}
