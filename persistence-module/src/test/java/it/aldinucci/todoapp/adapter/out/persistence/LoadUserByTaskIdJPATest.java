package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exception.AppTaskNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import(LoadUserByTaskIdJPA.class)
class LoadUserByTaskIdJPATest {

	@MockBean
	private AppGenericMapper<UserJPA, User> mapper;

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private LoadUserByTaskIdJPA adapter;
	
	@Test
	void test_loadUser_successful() throws AppTaskNotFoundException {
		UserJPA userJpa = new UserJPA("email", "username", "passwod");
		entityManager.persist(userJpa);
		ProjectJPA project = new ProjectJPA("project", userJpa);
		entityManager.persist(project);
		userJpa.getProjects().add(project);
		TaskJPA taskJpa = new TaskJPA("task", "task descr", true, project);
		entityManager.persist(taskJpa);
		project.getTasks().add(taskJpa);
		entityManager.flush();
		
		User user = new User();
		when(mapper.map(isA(UserJPA.class))).thenReturn(user);
		
		Optional<User> loadedUser = adapter.load(taskJpa.getId().toString());
		
		verify(mapper).map(userJpa);
		assertThat(loadedUser).containsSame(user);
	}
	
	@Test
	void test_loadUser_whenTaskNotPresent() {
		
		Optional<User> loadedUser = adapter.load("1");
		
		assertThat(loadedUser).isEmpty();
		verifyNoInteractions(mapper);
	}
	
	@Test
	void test_loadUser_whenIncalidId() {
		
		Optional<User> loadedUser = adapter.load("test");
		
		assertThat(loadedUser).isEmpty();
		verifyNoInteractions(mapper);
	}

}
