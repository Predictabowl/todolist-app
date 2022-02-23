package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exceptions.AppProjectNotFoundException;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import({ModelMapper.class, LoadTasksByProjectIdJPA.class})
class LoadTasksByProjectIdJPATest {

	@Autowired
	private LoadTasksByProjectIdJPA loadTasks;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private ModelMapper mapper;
	
	@Test
	void test_loadTasks_whenProjectNotPresent_shouldThrow() {
		assertThatThrownBy(() -> loadTasks.load(3L))
			.isInstanceOf(AppProjectNotFoundException.class)
			.hasMessage("Could not find project with id: 3");
	}
	
	@Test
	void test_loadTasks_whenNoTasksPresent() {
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
		
		List<Task> tasks = loadTasks.load(project1.getId());
		
		assertThat(tasks).isEmpty();
	}
	
	@Test
	void test_loadTasks_successful() {
		UserJPA user = new UserJPA("email", "username", "password");
		ProjectJPA project1 = new ProjectJPA("project 1", user);
		entityManager.persist(user);
		entityManager.persist(project1);
		user.getProjects().add(project1);
		TaskJPA task1 = new TaskJPA("task name", "task description", false, project1);
		TaskJPA task2 = new TaskJPA("task 2", "description 2", true, project1);
		entityManager.persist(task1);
		entityManager.persistAndFlush(task2);
		project1.getTasks().add(task1);
		project1.getTasks().add(task2);
		
		List<Task> tasks = loadTasks.load(project1.getId());
		
		assertThat(tasks).containsExactly(
				mapper.map(task1, Task.class),
				mapper.map(task2, Task.class));
	}

}
