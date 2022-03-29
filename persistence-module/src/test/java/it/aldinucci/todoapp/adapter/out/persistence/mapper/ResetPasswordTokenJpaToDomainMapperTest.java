package it.aldinucci.todoapp.adapter.out.persistence.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ResetPasswordTokenJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.domain.ResetPasswordToken;

class ResetPasswordTokenJpaToDomainMapperTest {

	@Test
	void test() {
		ResetPasswordTokenJpaToDomainMapper mapper = new ResetPasswordTokenJpaToDomainMapper();
		Date date = Calendar.getInstance().getTime();
		UUID uuid = UUID.randomUUID();
		ResetPasswordTokenJPA tokenJpa = new ResetPasswordTokenJPA(
				uuid,
				new UserJPA(2L, "email", "username", "password"),
				date);
		
		 ResetPasswordToken token = mapper.map(tokenJpa);
		
		assertThat(token.getToken()).matches(uuid.toString());
		assertThat(token.getExpiryDate()).isEqualTo(date);
		assertThat(token.getUserEmail()).matches("email");
	}

}
