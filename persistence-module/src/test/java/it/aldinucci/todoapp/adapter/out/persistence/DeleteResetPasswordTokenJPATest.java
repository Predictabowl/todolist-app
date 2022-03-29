package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ResetPasswordTokenJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import(DeleteResetPasswordTokenJPA.class)
class DeleteResetPasswordTokenJPATest {
	
	@Autowired
	private DeleteResetPasswordTokenJPA deleteToken;

	@Autowired
	TestEntityManager entityManager;

	@Test
	void test_deleteToken_successful() {
		UserJPA user = new UserJPA("email", "username", "pass");
		entityManager.persist(user);
		ResetPasswordTokenJPA token = new ResetPasswordTokenJPA(user, Calendar.getInstance().getTime());
		entityManager.persistAndFlush(token);

		deleteToken.delete(token.getToken().toString());

		List<ResetPasswordTokenJPA> tokens = entityManager.getEntityManager()
				.createQuery("from ResetPasswordTokenJPA", ResetPasswordTokenJPA.class).getResultList();

		assertThat(tokens).isEmpty();
	}
	
	@Test
	void test_deleteToken_whenNoToken() {
		deleteToken.delete("code");

		List<ResetPasswordTokenJPA> tokens = entityManager.getEntityManager()
				.createQuery("from ResetPasswordTokenJPA", ResetPasswordTokenJPA.class).getResultList();

		assertThat(tokens).isEmpty();
	}
	
	@Test
	void test_deleteToken_whenInvalidToken() {
		assertThatCode(() -> deleteToken.delete("code"))
			.doesNotThrowAnyException();

		List<ResetPasswordTokenJPA> tokens = entityManager.getEntityManager()
				.createQuery("from ResetPasswordTokenJPA", ResetPasswordTokenJPA.class).getResultList();

		assertThat(tokens).isEmpty();
	}

}
