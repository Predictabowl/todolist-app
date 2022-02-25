package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exceptions.AppProjectNotFoundException;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import({LoadUserByProjectIdJPA.class, ModelMapper.class})
class LoadUserByProjectIdJPATest {

	@Autowired
	private LoadUserByProjectIdJPA loadUser;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private ModelMapper mapper;
	
	@Test
	void test_loadUser_Successful() {
		UserJPA userJpa = new UserJPA("email", "username", "password");
		entityManager.persist(userJpa);
		ProjectJPA projectJpa = new ProjectJPA("project name", userJpa);
		entityManager.persist(projectJpa);
		userJpa.getProjects().add(projectJpa);
		
		User loadedUser = loadUser.load(projectJpa.getId());
		
		assertThat(loadedUser).usingRecursiveComparison().isEqualTo(mapper.map(userJpa, User.class));
	}
	
	@Test
	void test_loadUser_whenProjectNotPresent() {
		assertThatThrownBy(() -> loadUser.load(3L))
			.isInstanceOf(AppProjectNotFoundException.class)
			.hasMessage("Project not found with id: 3");
	}

}
