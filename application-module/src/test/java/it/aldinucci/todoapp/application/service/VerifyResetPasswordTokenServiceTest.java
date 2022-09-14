package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;

import it.aldinucci.todoapp.application.port.in.dto.StringTokenDTOIn;
import it.aldinucci.todoapp.application.port.out.DeleteRestPasswordTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadResetPasswordTokenDriverPort;
import it.aldinucci.todoapp.domain.ResetPasswordToken;

class VerifyResetPasswordTokenServiceTest {

	@Mock
	private LoadResetPasswordTokenDriverPort loadToken;
	
	@Mock
	private DeleteRestPasswordTokenDriverPort deleteToken;
	
	@InjectMocks
	private VerifyResetPasswordTokenService sut;

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
	void test_verify_whenTokenNotExists_isFalse() {
		when(loadToken.load(anyString())).thenReturn(Optional.empty());
		
		boolean result = sut.verify(new StringTokenDTOIn("token code"));
		
		assertThat(result).isFalse();
		verify(loadToken).load("token code");
		verifyNoInteractions(deleteToken);
	}
	
	@Test
	void test_verify_whenTokenIsExpired_isFalse() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -10);
		ResetPasswordToken token = new ResetPasswordToken("code", calendar.getTime(), "test@email.it");
		when(loadToken.load(anyString())).thenReturn(Optional.of(token));
		
		boolean result = sut.verify(new StringTokenDTOIn("code"));
		
		assertThat(result).isFalse();
		verify(loadToken).load("code");
		verify(deleteToken).delete("code");
	}
	
	@Test
	void test_verify_whenTokenIsValid_isTrue() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, 10);
		ResetPasswordToken token = new ResetPasswordToken("code", calendar.getTime(), "test@email.it");
		when(loadToken.load(anyString())).thenReturn(Optional.of(token));
		
		boolean result = sut.verify(new StringTokenDTOIn("code"));
		
		assertThat(result).isTrue();
		verify(loadToken).load("code");
		verifyNoInteractions(deleteToken);
	}

}
