package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.VerificationTokenJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.VerificationTokenJPARepository;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import(DeleteVerificationTokenJPA.class)
class DeleteVerificationTokenJPATest {

	@Autowired
	private DeleteVerificationTokenJPA deleteToken;

	@SpyBean
	private VerificationTokenJPARepository tokenRepo;
	
	@Autowired
	TestEntityManager entityManager;

	@Test
	void test_deleteToken_successful() {
		UserJPA user = new UserJPA("email", "username", "pass");
		entityManager.persist(user);
		VerificationTokenJPA token = new VerificationTokenJPA("code", user, Calendar.getInstance().getTime());
		entityManager.persistAndFlush(token);

		deleteToken.delete("code");

		List<VerificationTokenJPA> tokens = entityManager.getEntityManager()
				.createQuery("from VerificationTokenJPA", VerificationTokenJPA.class).getResultList();

		verify(tokenRepo).delete(token);
		assertThat(tokens).isEmpty();
	}
	
	@Test
	void test_deleteToken_whenTokenDontExists() {
		deleteToken.delete("code");

		List<VerificationTokenJPA> tokens = entityManager.getEntityManager()
				.createQuery("from VerificationTokenJPA", VerificationTokenJPA.class).getResultList();

		assertThat(tokens).isEmpty();
		verify(tokenRepo, times(0)).delete(any());
	}
	
}
