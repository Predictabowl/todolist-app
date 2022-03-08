package it.aldinucci.todoapp.application.service.util;


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

import it.aldinucci.todoapp.application.port.out.CreateUserVerificationTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.VerificationTokenData;
import it.aldinucci.todoapp.domain.VerificationToken;

class CreateVerificationTokenImplTest {

	private static final String FIXTURE_USER_EMAIL = "user@email.it";

	@Mock
	private CreateUserVerificationTokenDriverPort createTokenPort;
	
	@Mock
	private UniqueVerificationTokenGenerator stringGenerator;
	
	@Mock 
	private VerificationTokenExpiryDateGenerator dateGenerator;
	
	@InjectMocks
	private CreateVerificationTokenImpl tokenService;
	
	private Date date;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
		date = Calendar.getInstance().getTime();
	}
	
	@Test
	void test_createToken_success() {
		VerificationTokenData tokenDto = new VerificationTokenData("not so random string", date, FIXTURE_USER_EMAIL);
		when(stringGenerator.generate()).thenReturn("not so random string");
		when(dateGenerator.generate()).thenReturn(date);
		VerificationToken token = new VerificationToken("not so random string", date, "user@test.it");
		when(createTokenPort.create(isA(VerificationTokenData.class)))
			.thenReturn(token);
		
		VerificationToken createdToken = tokenService.create(FIXTURE_USER_EMAIL);
		
		InOrder inOrder = Mockito.inOrder(stringGenerator, dateGenerator, createTokenPort);
		inOrder.verify(stringGenerator).generate();
		inOrder.verify(dateGenerator).generate();
		inOrder.verify(createTokenPort).create(tokenDto);
		assertThat(createdToken).isSameAs(token);
				
	}
	
}
