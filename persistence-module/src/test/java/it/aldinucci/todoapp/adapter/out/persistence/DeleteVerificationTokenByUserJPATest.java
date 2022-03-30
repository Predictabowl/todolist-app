package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.VerificationTokenJPA;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import(DeleteVerificationTokenByUserJPA.class)
class DeleteVerificationTokenByUserJPATest {

	@Autowired
	private DeleteVerificationTokenByUserJPA deleteToken;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void test_delete_whenTokenMissing() {
		deleteToken.delete("email");

		List<VerificationTokenJPA> tokens = entityManager.getEntityManager()
				.createQuery("from VerificationTokenJPA", VerificationTokenJPA.class).getResultList();

		assertThat(tokens).isEmpty();
	}
	
	@Test
	void test_delete_whenTokenpresent() {
		UserJPA user = new UserJPA("email", "username", "pass");
		entityManager.persistAndFlush(user);
		VerificationTokenJPA token = new VerificationTokenJPA(user, Calendar.getInstance().getTime());
		entityManager.persistAndFlush(token);

		deleteToken.delete("email");

		List<VerificationTokenJPA> tokens = entityManager.getEntityManager()
				.createQuery("from VerificationTokenJPA", VerificationTokenJPA.class).getResultList();

		assertThat(tokens).isEmpty();
	}

}
