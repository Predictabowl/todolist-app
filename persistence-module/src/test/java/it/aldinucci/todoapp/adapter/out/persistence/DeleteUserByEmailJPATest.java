package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ResetPasswordTokenJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.VerificationTokenJPA;
import it.aldinucci.todoapp.domain.ResetPasswordToken;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import({ DeleteUserByEmailJPA.class })
class DeleteUserByEmailJPATest {

	@Autowired
	private DeleteUserByEmailJPA deleteUser;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void test_deleteUserShouldAlsoDeleteTokens() {
		UserJPA user = new UserJPA("email", "name", "pass");
		entityManager.persist(user);
		VerificationTokenJPA vToken = entityManager.persist(
				new VerificationTokenJPA("code", user, Calendar.getInstance().getTime()));
		ResetPasswordTokenJPA rpToken = entityManager.persist(
				new ResetPasswordTokenJPA("code", user, Calendar.getInstance().getTime()));
		entityManager.flush();

		deleteUser.delete("email");

		assertThat(entityManager.find(UserJPA.class, user.getId())).isNull();
		assertThat(entityManager.find(VerificationTokenJPA.class, vToken.getId())).isNull();
		assertThat(entityManager.find(ResetPasswordTokenJPA.class, rpToken.getId())).isNull();
	}

	@Test
	void test_deleteUser_whenTokenNotPresent() {
		UserJPA user = new UserJPA("email", "name", "pass");
		entityManager.persist(user);
		entityManager.flush();

		assertThatCode(() -> deleteUser.delete("email"))
			.doesNotThrowAnyException();

		assertThat(entityManager.find(UserJPA.class, user.getId())).isNull();
	}

	@Test
	void test_deleteUser_whenUserNotPresent() {
		assertThatCode(() -> deleteUser.delete("email"))
			.doesNotThrowAnyException();;
	}

}
