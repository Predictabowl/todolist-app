package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.util.ValidateId;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@DataJpaTest
@Import({LoadUserByProjectIdJPA.class})
class LoadUserByProjectIdJPATest {

	@MockBean
	private ValidateId<Long> validator;
	
	@Autowired
	private LoadUserByProjectIdJPA loadUser;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@MockBean
	private AppGenericMapper<UserJPA, User> mapper;
	
	@Test
	void test_loadUser_Successful() throws AppProjectNotFoundException {
		UserJPA userJpa = new UserJPA("email", "username", "password");
		entityManager.persist(userJpa);
		ProjectJPA projectJpa = new ProjectJPA("project name", userJpa);
		entityManager.persist(projectJpa);
		userJpa.getProjects().add(projectJpa);
		User user = new User();
		when(mapper.map(isA(UserJPA.class))).thenReturn(user);
		when(validator.getValidId(anyString()))
			.thenReturn(Optional.of(projectJpa.getId()));
		
		Optional<User> loadedUser = loadUser.load(projectJpa.getId().toString());
		
		verify(mapper).map(userJpa);
		verify(validator).getValidId(projectJpa.getId().toString());
		assertThat(loadedUser).containsSame(user);
	}
	
	@Test
	void test_loadUser_whenProjectNotPresent() {
		when(validator.getValidId(anyString())).thenReturn(Optional.of(3L));
		Optional<User> loadedUser = loadUser.load("3");

		assertThat(loadedUser).isEmpty();
		verifyNoInteractions(mapper);
		verify(validator).getValidId("3");
	}
	
	@Test
	void test_loadUser_whenInvalidId() {
		when(validator.getValidId(anyString())).thenReturn(Optional.empty());
		Optional<User> loadedUser = loadUser.load("test");

		assertThat(loadedUser).isEmpty();
		verifyNoInteractions(mapper);
		verify(validator).getValidId("test");
	}

}
