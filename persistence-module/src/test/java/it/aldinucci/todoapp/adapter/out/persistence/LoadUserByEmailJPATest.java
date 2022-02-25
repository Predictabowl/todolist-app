package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exceptions.AppUserNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import({LoadUserByEmailJPA.class})
class LoadUserByEmailJPATest {
	
	@MockBean
	private AppGenericMapper<UserJPA, User> mapper;
	
	@Autowired
	private LoadUserByEmailJPA loadAdapter;
	
	@Autowired
	private TestEntityManager entityManager;

	@Test
	void test_loadUser_successful() {
		UserJPA userJpa = new UserJPA("email", "username", "password");
		entityManager.persistAndFlush(userJpa);
		User user = new User();
		when(mapper.map(isA(UserJPA.class))).thenReturn(user);
		
		User loadedUser = loadAdapter.load("email");
		
		verify(mapper).map(userJpa);
		assertThat(loadedUser).isSameAs(user);
	}

	@Test
	void test_loadUser_whenUserNotPresent() {
		assertThatThrownBy(() -> loadAdapter.load("email"))
			.isExactlyInstanceOf(AppUserNotFoundException.class)
			.hasMessage("User not found with email: email");
		
		verifyNoInteractions(mapper);
	}
}
