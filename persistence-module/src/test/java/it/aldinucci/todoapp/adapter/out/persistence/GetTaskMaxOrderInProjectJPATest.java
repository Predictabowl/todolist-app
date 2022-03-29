package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.OptionalInt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import(GetTaskMaxOrderInProjectJPA.class)
class GetTaskMaxOrderInProjectJPATest {

	private static final String FIXTURE_EMAIL = "test@email.it";

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private GetTaskMaxOrderInProjectJPA sut;

	@Test
	void test_countWhenProjectNotExists_shouldThrow() {
		assertThatThrownBy(() -> sut.get("12"))
			.isInstanceOf(AppProjectNotFoundException.class)
			.hasMessage("Could not find Project with id: 12");
	}
	
	@Test
	void test_countWhenThereIsNoTask() {
		ProjectJPA projectJPA = setUpDB();
		
		OptionalInt maxValue = sut.get(projectJPA.getId().toString());
		
		assertThat(maxValue).isEmpty();
	}
	
	@Test
	void test_getWhenInvalidId() {
		OptionalInt maxValue = sut.get("test");
		
		assertThat(maxValue).isEmpty();
	}
	
	@Test
	void test_countSuccess() {
		ProjectJPA projectJPA = setUpDB();
		TaskJPA task1 = entityManager.persist(new TaskJPA(null,"task 1", "descr 1", false, projectJPA, 10));
		TaskJPA task2 = entityManager.persist(new TaskJPA(null, "task 2", "descr 2", true, projectJPA, 4));
		TaskJPA task3 = entityManager.persist(new TaskJPA(null, "task 3", "descr 3", false, projectJPA, 17));
		projectJPA.getTasks().addAll(Arrays.asList(task1, task2, task3));
		entityManager.flush();
		
		OptionalInt maxValue = sut.get(projectJPA.getId().toString());
		
		assertThat(maxValue).hasValue(17);
	}

	private ProjectJPA setUpDB() {
		UserJPA userJPA = new UserJPA(null, FIXTURE_EMAIL, "username", "password");
		entityManager.persist(userJPA);
		ProjectJPA projectJPA = new ProjectJPA("project name", userJPA);
		userJPA.getProjects().add(projectJPA);
		entityManager.persist(projectJPA);
		entityManager.flush();
		return projectJPA;
	}
}
