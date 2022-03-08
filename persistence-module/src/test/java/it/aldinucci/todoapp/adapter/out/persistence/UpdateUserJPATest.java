package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.application.port.out.dto.UserData;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exceptions.AppUserNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import(UpdateUserJPA.class)
class UpdateUserJPATest {

	private static final String FIXTURE_EMAIL = "test@email.it";

	@MockBean
	private AppGenericMapper<UserJPA, User> mapper;

	@Autowired
	private UpdateUserJPA updateUser;

	@Autowired
	TestEntityManager entityManager;

	@Test
	void test_updateWhenUserIsMissing_shouldThrow() {
		UserData dto = new UserData("user", FIXTURE_EMAIL, "password", true);
		
		assertThatThrownBy(() -> updateUser.update(dto))
			.isInstanceOf(AppUserNotFoundException.class)
			.hasMessage("Could not find user with email: test@email.it");
		
		verifyNoInteractions(mapper);
	}
	
	@Test
	void test_updateSuccess() {
		UserJPA userJpa = new UserJPA(null, FIXTURE_EMAIL, "name", "pass", false);
		entityManager.persistAndFlush(userJpa);
		UserData dto = new UserData("user", FIXTURE_EMAIL, "password", true);
		User user = new User();
		when(mapper.map(isA(UserJPA.class))).thenReturn(user);

		User updatedUser = updateUser.update(dto);

		assertThat(updatedUser).isSameAs(user);
		UserJPA userJPA2 = new UserJPA(userJpa.getId(), FIXTURE_EMAIL, "user", "password", true);
		verify(mapper).map(userJPA2);

		List<UserJPA> users = entityManager.getEntityManager().createQuery("from UserJPA", UserJPA.class)
				.getResultList();
		
		assertThat(users).hasSize(1);
		assertThat(users.get(0)).usingRecursiveComparison().isEqualTo(userJPA2);
	}

}
