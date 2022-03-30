package it.aldinucci.todoapp.application.service.util;

import static it.aldinucci.todoapp.config.ApplicationPropertyNames.RESET_PASSWORD_TOKEN_DURATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;

import it.aldinucci.todoapp.application.port.out.CreateResetPasswordTokenDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.ResetPasswordTokenData;
import it.aldinucci.todoapp.domain.ResetPasswordToken;

class CreateResetPasswordTokenImplTest {

	private static final String FIXTURE_EMAIL = "email@test.it";
	
	@Mock
	private CreateResetPasswordTokenDriverPort createResetToken;
	
//	@Mock
//	private UniqueResetPasswordTokenGenerator tokenStringGenerator;
	
	@Mock
	private TokenExpiryDateGenerator dateGenerator;
	
	private CreateResetPasswordTokenImpl sut;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
		sut = new CreateResetPasswordTokenImpl(createResetToken, dateGenerator);
	}

	
	@Test
	void test_createToken() {		
		ResetPasswordToken token = new ResetPasswordToken("code", Calendar.getInstance().getTime(), FIXTURE_EMAIL);
		when(createResetToken.create(any())).thenReturn(token);
		Date date = Calendar.getInstance().getTime();
		when(dateGenerator.generate(anyString(),anyInt())).thenReturn(date);
		
		ResetPasswordToken passwordToken = sut.create(FIXTURE_EMAIL);
		
		assertThat(passwordToken).isSameAs(token);
		InOrder inOrder = Mockito.inOrder(createResetToken, dateGenerator);
		inOrder.verify(dateGenerator).generate(RESET_PASSWORD_TOKEN_DURATION, 60);
		inOrder.verify(createResetToken).create(new ResetPasswordTokenData(date, FIXTURE_EMAIL));
	}

}
