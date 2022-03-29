package it.aldinucci.todoapp.adapter.out.persistence.mapper;

import static org.assertj.core.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.VerificationTokenJPA;
import it.aldinucci.todoapp.domain.VerificationToken;

class VerificationTokenJpaToDomainMapperTest {

	@Test
	void test() {
		UUID uuid = UUID.randomUUID();
		VerificationTokenJpaToDomainMapper mapper = new VerificationTokenJpaToDomainMapper();
		Date date = Calendar.getInstance().getTime();
		VerificationTokenJPA tokenJpa = new VerificationTokenJPA(
				uuid,
				new UserJPA(2L, "email", "username", "password"),
				date);
		
		VerificationToken token = mapper.map(tokenJpa);
		
		assertThat(token.getToken()).matches(uuid.toString());
		assertThat(token.getExpiryDate()).isEqualTo(date);
		assertThat(token.getUserEmail()).matches("email");
	}

}
