package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;
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
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import({DeleteProjectByIdJPA.class})
class DeleteProjectByIdJPATest {

	@Autowired
	private DeleteProjectByIdJPA deleteProject;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@MockBean
	private ValidateId<Long> validator;
	
	@Test
	void test_deleteProject_WhenNotPresent() {
		boolean deleted = deleteProject.delete("3");
		
		assertThat(deleted).isFalse();
	}
	
	@Test
	void test_deleteProject_WhenInvalidId() {
		when(validator.isValid(anyString())).thenReturn(false);
		boolean deleted = deleteProject.delete("invalid");
		
		assertThat(deleted).isFalse();
		verify(validator,times(0)).getId();
	}
	
	@Test
	void test_deleteProject_Successful() throws AppProjectNotFoundException {
		when(validator.isValid(anyString())).thenReturn(true);
		UserJPA user = new UserJPA("email", "username", "password");
		entityManager.persist(user);
		ProjectJPA project = new ProjectJPA("project name", user);
		entityManager.persist(project);
		user.getProjects().add(project);
		TaskJPA task = new TaskJPA("task name", "desc", false, project);
		entityManager.persistAndFlush(task);
		project.getTasks().add(task);
		when(validator.getId()).thenReturn(project.getId());
		
		boolean deleted = deleteProject.delete(project.getId().toString());
		
		assertThat(deleted).isTrue();
		assertThat(entityManager.find(ProjectJPA.class,project.getId())).isNull();
		assertThat(entityManager.find(TaskJPA.class, task.getId())).isNull();
		assertThat(user.getProjects()).isEmpty();
		verify(validator).isValid(project.getId().toString());
	}

}
