package it.aldinucci.todoapp.application.service.util;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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

import it.aldinucci.todoapp.application.port.out.DeleteVerificationTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadVerificationTokenDriverPort;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exceptions.AppCouldNotGenerateVerificationTokenException;
import it.aldinucci.todoapp.util.RandomStringGenerator;

class VerificationTokenStringGeneratorImplTest {

	private static final int TOKEN_LENGTH = 10;
	
	@Mock
	private RandomStringGenerator randStringGen;
	
	@Mock
	private LoadVerificationTokenDriverPort loadToken;
	
	@Mock
	private DeleteVerificationTokenDriverPort deleteToken;
	
	@InjectMocks
	private VerificationTokenStringGeneratorImpl tokenGenerator;
	
	@BeforeEach
	void sretUp() {
		openMocks(this);
	}
	
	@Test
	void test_tokenCreated_whenTokenDoesNotExists() {
		when(randStringGen.generate(anyInt())).thenReturn("random string");
		when(loadToken.load(isA(String.class))).thenReturn(Optional.empty());
		
		String generatedToken = tokenGenerator.generate(TOKEN_LENGTH);
		
		verify(randStringGen).generate(TOKEN_LENGTH);
		verify(loadToken).load("random string");
		verifyNoInteractions(deleteToken);
		assertThat(generatedToken).isEqualTo("random string");
	}
	
	@Test
	void test_tokenCreated_whenTokenNotUniqueAndNotExpired() {
		when(randStringGen.generate(anyInt()))
			.thenReturn("random string")
			.thenReturn("another string");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)+1);
		VerificationToken token = new VerificationToken("random string", calendar.getTime(), "user@test.it");
		when(loadToken.load(isA(String.class)))
			.thenReturn(Optional.of(token))
			.thenReturn(Optional.empty());
		
		String generatedString = tokenGenerator.generate(TOKEN_LENGTH);
		
		InOrder inOrder = Mockito.inOrder(randStringGen,loadToken);
		inOrder.verify(randStringGen).generate(TOKEN_LENGTH);
		inOrder.verify(loadToken).load("random string");
		inOrder.verify(randStringGen).generate(TOKEN_LENGTH);
		inOrder.verify(loadToken).load("another string");
		verifyNoInteractions(deleteToken);
		assertThat(generatedString).isEqualTo("another string");
	}
	
	@Test
	void test_tokenCreated_whenTokenExistsButIsExpired() {
		when(randStringGen.generate(anyInt()))
			.thenReturn("random string");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)-1);
		VerificationToken token = new VerificationToken("random string", calendar.getTime(), "user@test.it");
		when(loadToken.load(isA(String.class))).thenReturn(Optional.of(token));
		
		String generatedString = tokenGenerator.generate(TOKEN_LENGTH);
		
		verify(randStringGen).generate(TOKEN_LENGTH);
		verify(loadToken).load("random string");
		verify(deleteToken).delete("random string");
		verifyNoMoreInteractions(randStringGen);
		verifyNoMoreInteractions(loadToken);
		assertThat(generatedString).isEqualTo("random string");
	}
	
	@Test
	void test_tokenCreation_willThrow_ifMaximumNumberOfLoopsIsReached() {
	when(randStringGen.generate(anyInt())).thenReturn("random string");
	Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)+1);
	VerificationToken token = new VerificationToken("random string", calendar.getTime(), "user@test.it");
	when(loadToken.load(isA(String.class))).thenReturn(Optional.of(token));
	
	assertThatThrownBy(() -> tokenGenerator.generate(TOKEN_LENGTH))
		.isInstanceOf(AppCouldNotGenerateVerificationTokenException.class);
	
	}

}
