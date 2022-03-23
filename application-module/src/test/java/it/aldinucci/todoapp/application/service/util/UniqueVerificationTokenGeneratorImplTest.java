package it.aldinucci.todoapp.application.service.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.time.Duration;
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
import it.aldinucci.todoapp.exception.AppCouldNotGenerateTokenException;

class UniqueVerificationTokenGeneratorImplTest {

	@Mock
	private TokenStringGenerator randStringGen;
	
	@Mock
	private LoadVerificationTokenDriverPort loadToken;
	
	@Mock
	private DeleteVerificationTokenDriverPort deleteToken;
	
	@InjectMocks
	private UniqueVerificationTokenGeneratorImpl tokenGenerator;
	
	@BeforeEach
	void sretUp() {
		openMocks(this);
	}
	
	@Test
	void test_tokenCreated_whenTokenDoesNotExists() {
		when(randStringGen.generate()).thenReturn("random string");
		when(loadToken.load(isA(String.class))).thenReturn(Optional.empty());
		
		String generatedToken = tokenGenerator.generate();
		
		verify(randStringGen).generate();
		verify(loadToken).load("random string");
		verifyNoInteractions(deleteToken);
		assertThat(generatedToken).isEqualTo("random string");
	}
	
	@Test
	void test_tokenCreated_whenTokenNotUniqueAndNotExpired() {
		when(randStringGen.generate())
			.thenReturn("random string")
			.thenReturn("another string");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)+1);
		VerificationToken token = new VerificationToken("random string", calendar.getTime(), "user@test.it");
		when(loadToken.load(isA(String.class)))
			.thenReturn(Optional.of(token))
			.thenReturn(Optional.empty());
		
		String generatedString = tokenGenerator.generate();
		
		InOrder inOrder = Mockito.inOrder(randStringGen,loadToken);
		inOrder.verify(randStringGen).generate();
		inOrder.verify(loadToken).load("random string");
		inOrder.verify(randStringGen).generate();
		inOrder.verify(loadToken).load("another string");
		verifyNoInteractions(deleteToken);
		assertThat(generatedString).isEqualTo("another string");
	}
	
	@Test
	void test_tokenCreated_whenTokenExistsButIsExpired() {
		when(randStringGen.generate())
			.thenReturn("random string");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)-1);
		VerificationToken token = new VerificationToken("random string", calendar.getTime(), "user@test.it");
		when(loadToken.load(isA(String.class))).thenReturn(Optional.of(token));
		
		String generatedString = tokenGenerator.generate();
		
		verify(randStringGen).generate();
		verify(loadToken).load("random string");
		verify(deleteToken).delete("random string");
		verifyNoMoreInteractions(randStringGen);
		verifyNoMoreInteractions(loadToken);
		assertThat(generatedString).isEqualTo("random string");
	}
	
	@Test
	void test_tokenCreationNotUnique_willThrow_ifMaximumNumberOfLoopsIsReached() {
	when(randStringGen.generate()).thenReturn("random string");
	Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)+1);
	VerificationToken token = new VerificationToken("random string", calendar.getTime(), "user@test.it");
	when(loadToken.load(isA(String.class))).thenReturn(Optional.of(token));
	
	assertTimeoutPreemptively(Duration.ofSeconds(5), () -> 
		assertThatThrownBy(() -> tokenGenerator.generate())
			.isInstanceOf(AppCouldNotGenerateTokenException.class));
	
	}

}
