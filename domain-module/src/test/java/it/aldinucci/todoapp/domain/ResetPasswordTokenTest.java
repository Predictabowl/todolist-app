package it.aldinucci.todoapp.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResetPasswordTokenTest {

	private Calendar calendar;
	private ResetPasswordToken token;
	
	@BeforeEach
	void setUp() {
		calendar = Calendar.getInstance();
		token = new ResetPasswordToken("token", calendar.getTime(), "user@email.it");
	}
	
	@Test
	void test_isExpired() {
		calendar.add(Calendar.MINUTE, 1);
		Date date = calendar.getTime();
		
		assertThat(token.isExpired(date)).isTrue();
	}
	
	@Test
	void test_isNotExpired() {
		calendar.add(Calendar.MINUTE, -1);
		Date date = calendar.getTime();
		
		assertThat(token.isExpired(date)).isFalse();
	}
	
	@Test
	void test_whenDatesAreEqual_tokenIsNotExpired() {
		assertThat(token.isExpired(calendar.getTime())).isFalse();
	}

}
