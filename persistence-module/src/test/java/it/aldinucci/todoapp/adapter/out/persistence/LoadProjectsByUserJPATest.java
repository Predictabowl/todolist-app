package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@DataJpaTest
@Import({LoadProjectsByUserJPA.class})
class LoadProjectsByUserJPATest {
	
	private static final String TEST_EMAIL = "test@email.it";
	
	@MockBean
	private AppGenericMapper<ProjectJPA, Project> mapper;
	
	@Autowired
	private LoadProjectsByUserJPA loadProjects;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	void test_loadProjects_whenUserNotPresentShouldThrow() {
		assertThatThrownBy(() -> loadProjects.load(TEST_EMAIL))
			.isInstanceOf(AppUserNotFoundException.class)
			.hasMessage("User not found with email: "+TEST_EMAIL);
		
		verifyNoInteractions(mapper);
	}
	
	@Test
	void test_loadProjects_successful() throws AppUserNotFoundException {
		UserJPA user = new UserJPA(TEST_EMAIL, "username", "password");
		entityManager.persistAndFlush(user);
		ProjectJPA projectJpa1 = new ProjectJPA("project 1", user);
		ProjectJPA projectJpa2 = new ProjectJPA("project 2", user);
		entityManager.persist(projectJpa1);
		entityManager.persist(projectJpa2);
		user.getProjects().add(projectJpa1);
		user.getProjects().add(projectJpa2);
		Project project1 = new Project("1","name 1");
		Project project2 = new Project("2","name 2");
		when(mapper.map(isA(ProjectJPA.class)))
			.thenReturn(project1)
			.thenReturn(project2);
		
		List<Project> returnedProjects = loadProjects.load(TEST_EMAIL);
		
		verify(mapper).map(projectJpa1);
		verify(mapper).map(projectJpa2);
		verifyNoMoreInteractions(mapper);
		assertThat(returnedProjects).containsExactly(project1,project2);
	}

}
