package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import it.aldinucci.todoapp.adapter.out.persistence.util.ValidateId;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import({DeleteTaskByIdJPA.class})
class DeleteTaskByIdJPATest {

	@Autowired
	private DeleteTaskByIdJPA deleteTask;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@MockBean
	private ValidateId<Long> validator;
	
	@Test
	void test_deleteTask_successful() {
		when(validator.isValid(anyString())).thenReturn(true);
		UserJPA user = new UserJPA("email", "username", "password");
		entityManager.persist(user);
		ProjectJPA project = new ProjectJPA("project name", user);
		entityManager.persist(project);
		user.getProjects().add(project);
		TaskJPA task1 = new TaskJPA("task name", "desc", false, project);
		TaskJPA task2 = new TaskJPA("task 2", "descr 2", true, project);
		entityManager.persistAndFlush(task1);
		entityManager.persistAndFlush(task2);
		project.getTasks().add(task1);
		project.getTasks().add(task2);
		when(validator.getId()).thenReturn(task1.getId());
		
		boolean deleted = deleteTask.delete(task1.getId().toString());
		
		assertThat(deleted).isTrue();
		assertThat(entityManager.find(TaskJPA.class, task1.getId())).isNull();
		assertThat(project.getTasks()).containsExactly(task2);
		verify(validator).isValid(task1.getId().toString());
	}
	
	@Test
	void test_deleteTask_whenTaskMissing() {
		when(validator.isValid(anyString())).thenReturn(true);
		when(validator.getId()).thenReturn(1L);
		
		boolean deleted = deleteTask.delete("1");
		
		assertThat(deleted).isFalse();
		verify(validator).isValid("1");
	}
	
	@Test
	void test_deleteTask_whenInvalidId() {
		when(validator.isValid(anyString())).thenReturn(false);
		
		boolean deleted = deleteTask.delete("test");
		
		assertThat(deleted).isFalse();
		verify(validator, times(0)).getId();
	}

}
