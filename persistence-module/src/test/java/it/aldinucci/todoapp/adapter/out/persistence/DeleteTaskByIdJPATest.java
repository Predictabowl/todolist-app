package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
import it.aldinucci.todoapp.exception.AppTaskNotFoundException;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import({DeleteTaskByIdJPA.class})
class DeleteTaskByIdJPATest {

	@Autowired
	private DeleteTaskByIdJPA deleteTask;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	void test_deleteTask_successful() {
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
		
		deleteTask.delete(task1.getId());
		
		assertThat(entityManager.find(TaskJPA.class, task1.getId())).isNull();
		assertThat(project.getTasks()).containsExactly(task2);
	}
	
	@Test
	void test_deleteTask_whenTaskMissing() {
		
		assertThatCode(() -> deleteTask.delete(1))
			.doesNotThrowAnyException();
	}

}
