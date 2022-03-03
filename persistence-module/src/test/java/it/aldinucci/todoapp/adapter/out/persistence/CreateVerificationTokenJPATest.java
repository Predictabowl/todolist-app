package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.VerificationTokenJPA;
import it.aldinucci.todoapp.application.port.out.dto.VerificationTokenDTOOut;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import(CreateVerificationTokenJPA.class)
class CreateVerificationTokenJPATest {

	@Autowired
	private CreateVerificationTokenJPA createToken;

	@Autowired
	private TestEntityManager entityManager;
	
	private Date date;

	@BeforeEach
	void setUp() {
		date = Calendar.getInstance().getTime();
	}

	@Test
	void test_createToken_whenDoesNotExists() {
		UserJPA user = new UserJPA("user@email.it", "username", "pass");
		entityManager.persistAndFlush(user);
		VerificationTokenDTOOut dto = new VerificationTokenDTOOut("token", date, "user@email.it");

		createToken.create(dto);

		List<VerificationTokenJPA> tokens = entityManager.getEntityManager()
				.createQuery("from VerificationTokenJPA", VerificationTokenJPA.class).getResultList();
		
		assertThat(tokens).hasSize(1);
		VerificationTokenJPA tokenJPA = tokens.get(0);
		assertThat(tokenJPA.getExpiryDate()).isEqualTo(date);
		assertThat(tokenJPA.getToken()).isEqualTo("token");
		assertThat(tokenJPA.getUser()).isEqualTo(user);
	}
	
}
