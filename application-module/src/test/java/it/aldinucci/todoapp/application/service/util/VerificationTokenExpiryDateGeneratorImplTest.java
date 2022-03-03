package it.aldinucci.todoapp.application.service.util;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.env.Environment;

import it.aldinucci.todoapp.application.util.ApplicationPropertyNames;

class VerificationTokenExpiryDateGeneratorImplTest {

	private static final int FIXTURE_TOKEN_DURATION = 25;

	@Mock
	private Environment env;

	@InjectMocks
	private VerificationTokenExpiryDateGeneratorImpl dateGen;

	private Calendar calendar;

	@BeforeEach
	void setUp() {
		openMocks(this);
		calendar = Calendar.getInstance();
		when(env.getProperty(isA(String.class),any(),anyInt())).thenReturn(FIXTURE_TOKEN_DURATION);
	}

	@Test
	void test_DateCalculation() {
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)+FIXTURE_TOKEN_DURATION);
		
		Date generatedDate = dateGen.generate();
		
		assertThat(generatedDate)
			.isInSameHourWindowAs(Calendar.getInstance().getTime())
			.hasMinute(calendar.get(Calendar.MINUTE));
		
		verify(env).getProperty(
				ApplicationPropertyNames.VERIFICATION_TOKEN_DURATION,
				Integer.class,
				VerificationTokenExpiryDateGeneratorImpl.DEFAULT_TOKEN_DURATION);
	}

}
