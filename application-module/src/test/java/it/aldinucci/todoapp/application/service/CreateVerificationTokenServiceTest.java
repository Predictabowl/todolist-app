package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
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

import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.CreateVerificationTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.DeleteVerificatinTokenByUserDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.VerificationTokenDTOOut;
import it.aldinucci.todoapp.application.service.util.UniqueVerificationTokenGenerator;
import it.aldinucci.todoapp.application.service.util.VerificationTokenExpiryDateGenerator;
import it.aldinucci.todoapp.domain.VerificationToken;

class CreateVerificationTokenServiceTest {

	@Mock
	private CreateVerificationTokenDriverPort createTokenPort;
	
	@Mock
	private UniqueVerificationTokenGenerator stringGenerator;
	
	@Mock
	private DeleteVerificatinTokenByUserDriverPort deleteTokenPort;
	
	@Mock VerificationTokenExpiryDateGenerator dateGenerator;
	
	@InjectMocks
	private CreateVerificationTokenService tokenService;
	
	private Date date;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
		date = Calendar.getInstance().getTime();
	}
	
	@Test
	void test_createTokenUsingProperties_success() {
		VerificationTokenDTOOut tokenDto = new VerificationTokenDTOOut("not so random string", date, "user@email.it");
		when(stringGenerator.generate()).thenReturn("not so random string");
		when(dateGenerator.generate()).thenReturn(date);
		VerificationToken token = new VerificationToken("not so random string", date, "user@test.it");
		when(createTokenPort.create(isA(VerificationTokenDTOOut.class)))
			.thenReturn(token);
		
		VerificationToken createdToken = tokenService.create(new UserIdDTO("user@email.it"));
		
		InOrder inOrder = Mockito.inOrder(stringGenerator, dateGenerator, createTokenPort, deleteTokenPort);
		inOrder.verify(deleteTokenPort).delete("user@email.it");
		inOrder.verify(stringGenerator).generate();
		inOrder.verify(dateGenerator).generate();
		inOrder.verify(createTokenPort).create(tokenDto);
		assertThat(createdToken).isSameAs(token);
				
	}

}
