package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ResetPasswordTokenJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.util.ValidateId;

@DataJpaTest
@Import(DeleteResetPasswordTokenJPA.class)
class DeleteResetPasswordTokenJPATest {
	
	@Autowired
	private DeleteResetPasswordTokenJPA deleteToken;
	
	@MockBean
	private ValidateId<UUID> validator;

	@Autowired
	TestEntityManager entityManager;

	@Test
	void test_deleteToken_successful() {
		UserJPA user = new UserJPA("email", "username", "pass");
		entityManager.persist(user);
		ResetPasswordTokenJPA token = new ResetPasswordTokenJPA(user, Calendar.getInstance().getTime());
		entityManager.persistAndFlush(token);
		when(validator.getValidId(anyString())).thenReturn(Optional.of(token.getToken()));

		deleteToken.delete("some id");

		List<ResetPasswordTokenJPA> tokens = entityManager.getEntityManager()
				.createQuery("from ResetPasswordTokenJPA", ResetPasswordTokenJPA.class).getResultList();

		assertThat(tokens).isEmpty();
		verify(validator).getValidId("some id");
	}
	
	@Test
	void test_deleteToken_whenNoTokenPresent() {
		when(validator.getValidId(anyString()))
			.thenReturn(Optional.of(UUID.randomUUID()));
		
		deleteToken.delete("some id");

		List<ResetPasswordTokenJPA> tokens = entityManager.getEntityManager()
				.createQuery("from ResetPasswordTokenJPA", ResetPasswordTokenJPA.class).getResultList();

		assertThat(tokens).isEmpty();
		verify(validator).getValidId("some id");
	}
	
	@Test
	void test_deleteToken_whenInvalidToken() {
		when(validator.getValidId(anyString())).thenReturn(Optional.empty());
		
		assertThatCode(() -> deleteToken.delete("invalid id"))
			.doesNotThrowAnyException();

		List<ResetPasswordTokenJPA> tokens = entityManager.getEntityManager()
				.createQuery("from ResetPasswordTokenJPA", ResetPasswordTokenJPA.class).getResultList();

		assertThat(tokens).isEmpty();
		verify(validator).getValidId("invalid id");
	}

}
