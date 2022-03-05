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

import it.aldinucci.todoapp.config.ApplicationPropertyNames;
import it.aldinucci.todoapp.util.AppPropertiesReader;

class VerificationTokenExpiryDateGeneratorImplTest {

	private static final int FIXTURE_TOKEN_DURATION = 25;

	@Mock
	private AppPropertiesReader propReader;

	@InjectMocks
	private VerificationTokenExpiryDateGeneratorImpl dateGen;

	private Calendar calendar;

	@BeforeEach
	void setUp() {
		openMocks(this);
		calendar = Calendar.getInstance();
		when(propReader.get(isA(String.class),any(),anyInt())).thenReturn(FIXTURE_TOKEN_DURATION);
	}

	@Test
	void test_DateCalculation() {
		calendar.add(Calendar.MINUTE, FIXTURE_TOKEN_DURATION);
		
		Date generatedDate = dateGen.generate();
		
		assertThat(generatedDate)
			.isInSameHourWindowAs(Calendar.getInstance().getTime())
			.hasMinute(calendar.get(Calendar.MINUTE));
		
		verify(propReader).get(
				ApplicationPropertyNames.VERIFICATION_TOKEN_DURATION,
				Integer.class,
				VerificationTokenExpiryDateGeneratorImpl.DEFAULT_TOKEN_DURATION);
	}

}
