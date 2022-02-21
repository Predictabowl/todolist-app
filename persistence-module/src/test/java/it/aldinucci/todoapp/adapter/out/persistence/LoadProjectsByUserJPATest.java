package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.exceptions.UserPersistenceException;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import({LoadProjectsByUserJPA.class, ModelMapper.class})
class LoadProjectsByUserJPATest {
	
	private static final String TEST_EMAIL = "test@email.it";
	
	@Autowired
	private LoadProjectsByUserJPA loadProjects;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Autowired
	ModelMapper mapper;

	@Test
	void test_loadProjects_whenUserNotPresentShouldThrow() {
		assertThatThrownBy(() -> loadProjects.load(TEST_EMAIL))
			.isInstanceOf(UserPersistenceException.class)
			.hasMessage("User not found with email: "+TEST_EMAIL);
	}
	
	@Test
	void test_loadProjects_successful() {
		UserJPA user = new UserJPA(TEST_EMAIL, "username", "password");
		entityManager.persistAndFlush(user);
		ProjectJPA projectJpa1 = new ProjectJPA("project 1", user);
		ProjectJPA projectJpa2 = new ProjectJPA("project 2", user);
		entityManager.persist(projectJpa1);
		entityManager.persist(projectJpa2);
		user.getProjects().add(projectJpa1);
		user.getProjects().add(projectJpa2);
		
		List<Project> returnedProjects = loadProjects.load(TEST_EMAIL);
		
		assertThat(returnedProjects).containsExactly(
				mapper.map(projectJpa1, Project.class),
				mapper.map(projectJpa2, Project.class));
	}

}
