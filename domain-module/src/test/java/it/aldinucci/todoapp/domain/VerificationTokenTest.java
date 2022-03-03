package it.aldinucci.todoapp.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VerificationTokenTest {

	private VerificationToken token;
	private Calendar calendar;
	
	@BeforeEach
	void setUp() {
		token = new VerificationToken("token", Calendar.getInstance().getTime(), "user@email.it");
		calendar = Calendar.getInstance();
	}
	
	@Test
	void test_isExpired() {
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)+1);
		Date date = calendar.getTime();
		
		assertThat(token.isExpired(date)).isTrue();
	}
	
	@Test
	void test_isNotExpired() {
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)-1);
		Date date = calendar.getTime();
		
		assertThat(token.isExpired(date)).isFalse();
	}

}
