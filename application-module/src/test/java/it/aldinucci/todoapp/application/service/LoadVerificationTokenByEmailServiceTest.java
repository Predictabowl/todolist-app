package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Calendar;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadVerificationTokenByEmailDriverPort;
import it.aldinucci.todoapp.domain.VerificationToken;

class LoadVerificationTokenByEmailServiceTest {

	@Mock
	private LoadVerificationTokenByEmailDriverPort loadToken;
	
	@InjectMocks
	private LoadVerificationTokenByEmailService serviceSut;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
	}
	
	@Test
	void test_loadToken() {
		VerificationToken token = new VerificationToken("code", Calendar.getInstance().getTime(), "user@email.it");
		when(loadToken.load("user@email.it")).thenReturn(Optional.of(token));
		
		Optional<VerificationToken> loadedToken = serviceSut.load(new UserIdDTO("user@email.it"));
		
		assertThat(loadedToken).containsSame(token);
	}

}
