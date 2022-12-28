package it.aldinucci.todoapp.adapter.out.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;

@DataJpaTest
class TaskJPARepositoryTest{

	@Autowired
	private TaskJPARepository repository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	void test_findByProjectIdAndNotCompleted_successful() {
		UserJPA user = new UserJPA("email", "username", "password");
		entityManager.persist(user);
		ProjectJPA project1 = new ProjectJPA("first project", user);
		ProjectJPA project2 = new ProjectJPA("second project", user);
		entityManager.persist(project1);
		entityManager.persist(project2);
		user.getProjects().add(project1);
		user.getProjects().add(project2);
		
		TaskJPA task1 = new TaskJPA("first task", "", false, project1);
		TaskJPA task2 = new TaskJPA("second task", "", true, project1);
		TaskJPA task3 = new TaskJPA("third task", "", false, project2);
		TaskJPA task4 = new TaskJPA("fourth task", "", false, project1);
		entityManager.persist(task1);
		entityManager.persist(task2);
		entityManager.persist(task3);
		entityManager.persist(task4);
		project1.getTasks().add(task1);
		project1.getTasks().add(task2);
		project2.getTasks().add(task3);
		project1.getTasks().add(task4);
		entityManager.flush();
		
		
		List<TaskJPA> result = repository.findByProjectIdAndCompletedFalse(project1.getId());
		
		assertThat(result).containsExactly(task1,task4);
	}
	
}
