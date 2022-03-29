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
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import({ LoadTasksByProjectIdJPA.class })
class LoadTasksByProjectIdJPATest {

	@MockBean
	private AppGenericMapper<TaskJPA, Task> mapper;

	@Autowired
	private LoadTasksByProjectIdJPA loadTasks;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void test_loadTasks_whenProjectNotPresent_shouldThrow() {
		assertThatThrownBy(() -> loadTasks.load("3")).isInstanceOf(AppProjectNotFoundException.class)
				.hasMessage("Could not find project with id: 3");
	}

	@Test
	void test_loadTasks_whenNoTasksPresent() throws AppProjectNotFoundException {
		UserJPA user = new UserJPA("email", "username", "password");
		ProjectJPA project1 = new ProjectJPA("project 1", user);
		ProjectJPA project2 = new ProjectJPA("project 2", user);
		entityManager.persist(user);
		entityManager.persist(project1);
		entityManager.persist(project2);
		user.getProjects().add(project1);
		user.getProjects().add(project2);

		TaskJPA task = new TaskJPA("task name", "task description", false, project2);
		entityManager.persistAndFlush(task);
		project2.getTasks().add(task);

		List<Task> tasks = loadTasks.load(project1.getId().toString());

		verifyNoInteractions(mapper);
		assertThat(tasks).isEmpty();
	}

	@Test
	void test_loadTasks_successful() throws AppProjectNotFoundException {
		UserJPA user = new UserJPA("email", "username", "password");
		ProjectJPA project1 = new ProjectJPA("project 1", user);
		entityManager.persist(user);
		entityManager.persist(project1);
		user.getProjects().add(project1);
		TaskJPA taskJpa1 = new TaskJPA("task name", "task description", false, project1);
		TaskJPA taskJpa2 = new TaskJPA("task 2", "description 2", true, project1);
		entityManager.persist(taskJpa1);
		entityManager.persistAndFlush(taskJpa2);
		project1.getTasks().add(taskJpa1);
		project1.getTasks().add(taskJpa2);
		Task task1 = new Task("2L", "task1", "", false);
		Task task2 = new Task("4L", "task2", "descr", true);
		when(mapper.map(isA(TaskJPA.class))).thenReturn(task1).thenReturn(task2);

		List<Task> tasks = loadTasks.load(project1.getId().toString());

		verify(mapper).map(taskJpa1);
		verify(mapper).map(taskJpa2);
		verifyNoMoreInteractions(mapper);
		assertThat(tasks).containsExactly(task1, task2);
	}

}
