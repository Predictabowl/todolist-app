package it.aldinucci.todoapp.adapter.out.persistence.mapper;

import static org.assertj.core.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.VerificationTokenJPA;
import it.aldinucci.todoapp.domain.VerificationToken;

class VerificationTokenJpaToDomainMapperTest {

	@Test
	void test() {
		VerificationTokenJpaToDomainMapper mapper = new VerificationTokenJpaToDomainMapper();
		Date date = Calendar.getInstance().getTime();
		VerificationTokenJPA tokenJpa = new VerificationTokenJPA(
				3L, 
				"random",
				new UserJPA(2L, "email", "username", "password"),
				date);
		
		VerificationToken token = mapper.map(tokenJpa);
		
		assertThat(token.getToken()).isEqualTo("random");
		assertThat(token.getExpiryDate()).isEqualTo(date);
		assertThat(token.getUserEmail()).isEqualTo("email");
	}

}
