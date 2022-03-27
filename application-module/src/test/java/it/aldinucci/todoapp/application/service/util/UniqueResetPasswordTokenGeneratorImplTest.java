package it.aldinucci.todoapp.application.service.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.time.Duration;
import java.util.Calendar;
import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;

import it.aldinucci.todoapp.application.port.out.DeleteRestPasswordTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadResetPasswordTokenDriverPort;
import it.aldinucci.todoapp.domain.ResetPasswordToken;
import it.aldinucci.todoapp.exception.AppCouldNotGenerateTokenException;

class UniqueResetPasswordTokenGeneratorImplTest {

	@Mock
	private TokenStringGenerator randStringGen;
	
	@Mock
	private LoadResetPasswordTokenDriverPort loadToken;
	
	@Mock
	private DeleteRestPasswordTokenDriverPort deleteToken;
	
	private UniqueResetPasswordTokenGeneratorImpl sut;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
		sut = new UniqueResetPasswordTokenGeneratorImpl(randStringGen, loadToken, deleteToken);
	}
	
	@Test
	void test_tokenCreated_whenTokenDoesNotExists() {
		when(randStringGen.generate()).thenReturn("random string");
		when(loadToken.load(isA(String.class))).thenReturn(Optional.empty());
		
		String generatedToken = sut.generate();
		
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
		ResetPasswordToken token = new ResetPasswordToken("random string", calendar.getTime(), "user@test.it");
		when(loadToken.load(anyString()))
			.thenReturn(Optional.of(token))
			.thenReturn(Optional.empty());
		
		String generatedString = sut.generate();
		
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
		ResetPasswordToken token = new ResetPasswordToken("random string", calendar.getTime(), "user@test.it");
		when(loadToken.load(isA(String.class))).thenReturn(Optional.of(token));
		
		String generatedString = sut.generate();
		
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
		ResetPasswordToken token = new ResetPasswordToken("random string", calendar.getTime(), "user@test.it");
		when(loadToken.load(anyString())).thenReturn(Optional.of(token));
		
		assertTimeoutPreemptively(Duration.ofSeconds(5), () -> 
			assertThatThrownBy(() -> sut.generate())
				.isInstanceOf(AppCouldNotGenerateTokenException.class));
	
	}
	
	@Test
	void test_tokenCreationNotUnique_maximumNumberOfLoops_boundaryTest() {
		int numIterations = 2048;
		String[] arguments = IntStream.range(0, numIterations).mapToObj(i -> "token").toList().toArray(new String[0]);
		arguments[numIterations-1] = "different token";
		when(randStringGen.generate()).thenReturn("token",arguments);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)+1);
		ResetPasswordToken token = new ResetPasswordToken("random string", calendar.getTime(), "user@test.it");
		when(loadToken.load("token")).thenReturn(Optional.of(token));
		when(loadToken.load("different token")).thenReturn(Optional.empty());
		
		assertTimeoutPreemptively(Duration.ofSeconds(5), () -> 
			assertThatThrownBy(() -> sut.generate())
				.isInstanceOf(AppCouldNotGenerateTokenException.class));
	}
}
