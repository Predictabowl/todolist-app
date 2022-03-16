package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import(UpdateTaskJPA.class)
class UpdateTaskJPATest {

	@Autowired
	private UpdateTaskJPA sut;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@MockBean
	private AppGenericMapper<TaskJPA, Task> mapper;
	
	private ProjectJPA project;
	
	@BeforeEach
	void setUp() {
		UserJPA user = new UserJPA("email@test.it", "username", "password");
		project = new ProjectJPA("project name", user);
		entityManager.persist(user);
		entityManager.persist(project);
		user.getProjects().add(project);		
		
		entityManager.flush();
	}
	
	
	@Test
	void test_updateTask_whenTaskDontExists() {
		Task task = new Task(1L, "another name", "test", true);
		
		Optional<Task> optionalTask = sut.update(task);
		
		assertThat(optionalTask).isEmpty();
		verifyNoInteractions(mapper);
	}
	
	@Test
	void test_updateTask_success() {
		TaskJPA taskJpa = new TaskJPA("task name", "description", false, project);
		entityManager.persist(taskJpa);
		project.getTasks().add(taskJpa);
		entityManager.flush();
		Task emptyTask = new Task();
		when(mapper.map(isA(TaskJPA.class))).thenReturn(emptyTask);
		
		Task task = new Task(taskJpa.getId(), "another name", "test", true);
		
		Optional<Task> updatedTask = sut.update(task);
		
		assertThat(taskJpa.getDescription()).matches("test");
		assertThat(taskJpa.getName()).matches("another name");
		assertThat(taskJpa.isCompleted()).isTrue();
		
		verify(mapper).map(taskJpa);
		assertThat(updatedTask).containsSame(emptyTask);
	}


}
