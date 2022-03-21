package it.aldinucci.todoapp.adapter.out.persistence.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ResetPasswordTokenJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.domain.ResetPasswordToken;

class ResetPasswordTokenJpaToDomainMapperTest {

	@Test
	void test() {
		ResetPasswordTokenJpaToDomainMapper mapper = new ResetPasswordTokenJpaToDomainMapper();
		Date date = Calendar.getInstance().getTime();
		ResetPasswordTokenJPA tokenJpa = new ResetPasswordTokenJPA(
				3L, 
				"random",
				new UserJPA(2L, "email", "username", "password"),
				date);
		
		 ResetPasswordToken token = mapper.map(tokenJpa);
		
		assertThat(token.getToken()).isEqualTo("random");
		assertThat(token.getExpiryDate()).isEqualTo(date);
		assertThat(token.getUserEmail()).isEqualTo("email");
	}

}
