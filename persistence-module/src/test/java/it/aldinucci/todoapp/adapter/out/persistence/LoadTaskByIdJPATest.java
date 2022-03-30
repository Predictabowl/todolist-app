package it.aldinucci.todoapp.adapter.out.persistence;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

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
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import(LoadTaskByIdJPA.class)
class LoadTaskByIdJPATest {

	@Autowired
	TestEntityManager entityManager;
	
	@Autowired
	private LoadTaskByIdJPA sut;
	
	@MockBean
	private AppGenericMapper<TaskJPA, Task> mapper;
	
	@MockBean
	private ValidateId<Long> validator;

	private ProjectJPA project;

	@Test
	void test_loadTask_whenMissing() {
		when(validator.isValid(anyString())).thenReturn(true);
		when(validator.getId()).thenReturn(1L);
		Optional<Task> loadedTask = sut.load("1");
		
		assertThat(loadedTask).isEmpty();
		verify(validator).isValid("1");
	}
	
	@Test
	void test_loadTask_whenInvalidId() {
		when(validator.isValid(anyString())).thenReturn(false);
		Optional<Task> loadedTask = sut.load("test");
		
		assertThat(loadedTask).isEmpty();
		
		verify(validator).isValid("test");
		verify(validator, times(0)).getId();
	}
	
	
	@Test
	void test_loadTask_success() {
		when(validator.isValid(anyString())).thenReturn(true);
		setUpDb();
		TaskJPA taskJpa = new TaskJPA("name", "test", false, project);
		entityManager.persist(taskJpa);
		project.getTasks().add(taskJpa);
		entityManager.flush();
		Task task = new Task();
		when(mapper.map(isA(TaskJPA.class))).thenReturn(task);
		when(validator.getId()).thenReturn(taskJpa.getId());
		
		Optional<Task> loadedTask = sut.load(taskJpa.getId().toString());
		
		verify(mapper).map(taskJpa);
		verify(validator).isValid(taskJpa.getId().toString());
		assertThat(loadedTask).containsSame(task);
	}
	
	private void setUpDb() {
		UserJPA user = new UserJPA("email@test.it", "username", "password");
		project = new ProjectJPA("project name", user);
		entityManager.persist(user);
		entityManager.persist(project);
		user.getProjects().add(project);
		entityManager.flush();
	}

}
