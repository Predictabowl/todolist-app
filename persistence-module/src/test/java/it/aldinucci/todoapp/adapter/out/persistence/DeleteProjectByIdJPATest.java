package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;

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
import it.aldinucci.todoapp.exceptions.ProjectNotFoundException;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import({DeleteProjectByIdJPA.class})
class DeleteProjectByIdJPATest {

	@Autowired
	private DeleteProjectByIdJPA deleteProject;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	void test_deleteProject_WhenNotPresent() {
		assertThatThrownBy(() -> deleteProject.delete(3L))
			.isInstanceOf(ProjectNotFoundException.class)
			.hasMessage("Could not find Project with id: 3");
	}
	
	@Test
	void test_deleteProject_Successful() {
		UserJPA user = new UserJPA("email", "username", "password");
		entityManager.persist(user);
		ProjectJPA project = new ProjectJPA("project name", user);
		entityManager.persist(project);
		user.getProjects().add(project);
		TaskJPA task = new TaskJPA("task name", "desc", false, project);
		entityManager.persistAndFlush(task);
		project.getTasks().add(task);
		
		deleteProject.delete(project.getId());
		
		assertThat(entityManager.find(ProjectJPA.class,project.getId())).isNull();
		assertThat(entityManager.find(TaskJPA.class, task.getId())).isNull();
		assertThat(user.getProjects()).isEmpty();
	}

}
