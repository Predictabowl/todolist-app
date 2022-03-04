package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import it.aldinucci.todoapp.application.port.out.CreateVerificationTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.DeleteVerificatinTokenByUserDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.VerificationTokenDTOOut;
import it.aldinucci.todoapp.application.service.util.VerificationTokenExpiryDateGenerator;
import it.aldinucci.todoapp.application.service.util.VerificationTokenStringGenerator;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.util.AppPropertiesReader;
import it.aldinucci.todoapp.util.ApplicationPropertyNames;

class CreateVerificationTokenServiceTest {

	private static final int FIXTURE_TOKEN_LENGTH = 10;

	@Mock
	private CreateVerificationTokenDriverPort createTokenPort;
	
	@Mock
	private VerificationTokenStringGenerator stringGenerator;
	
	@Mock
	private DeleteVerificatinTokenByUserDriverPort deleteTokenPort;
	
	@Mock
	private AppPropertiesReader propReader;
	
	@Mock VerificationTokenExpiryDateGenerator dateGenerator;
	
	@InjectMocks
	private CreateVerificationTokenService tokenService;
	
	private Date date;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
		date = Calendar.getInstance().getTime();
		when(propReader.get(isA(String.class), any(), anyInt())).thenReturn(FIXTURE_TOKEN_LENGTH);
	}
	
	@Test
	void test_createTokenUsingProperties_success() {
		User user = new User("user@email.it", "name", "pass");
		VerificationTokenDTOOut tokenDto = new VerificationTokenDTOOut("not so random string", date, "user@email.it");
		when(stringGenerator.generate(anyInt())).thenReturn("not so random string");
		when(dateGenerator.generate()).thenReturn(date);
		VerificationToken token = new VerificationToken("not so random string", date, "user@test.it");
		when(createTokenPort.create(isA(VerificationTokenDTOOut.class)))
			.thenReturn(token);
		
		VerificationToken createdToken = tokenService.create(user);
		
		InOrder inOrder = Mockito.inOrder(stringGenerator, dateGenerator, createTokenPort, deleteTokenPort);
		inOrder.verify(deleteTokenPort).delete("user@email.it");
		inOrder.verify(stringGenerator).generate(FIXTURE_TOKEN_LENGTH);
		inOrder.verify(dateGenerator).generate();
		inOrder.verify(createTokenPort).create(tokenDto);
		verify(propReader).get(
				ApplicationPropertyNames.VERIFICATION_TOKEN_LENGTH,
				Integer.class,
				CreateVerificationTokenService.DEFAULT_TOKEN_LENGTH);
		assertThat(createdToken).isSameAs(token);
				
	}

}
