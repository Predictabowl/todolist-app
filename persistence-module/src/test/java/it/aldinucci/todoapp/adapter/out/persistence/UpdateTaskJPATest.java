package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
@Import(UpdateTaskJPA.class)
class UpdateTaskJPATest {

	@Autowired
	private UpdateTaskJPA sut;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@MockBean
	private AppGenericMapper<TaskJPA, Task> mapper;
	
	@MockBean
	private ValidateId<Long> validator;
	
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
		when(validator.isValid(anyString())).thenReturn(Optional.of(1L));
		Task task = new Task("1", "another name", "test", true);
		
		Optional<Task> optionalTask = sut.update(task);
		
		assertThat(optionalTask).isEmpty();
		verifyNoInteractions(mapper);
		verify(validator).isValid("1");
	}
	
	@Test
	void test_updateTask_whenInvalidId() {
		when(validator.isValid(anyString())).thenReturn(Optional.empty());
		Task task = new Task("test", "another name", "test", true);
		
		Optional<Task> optionalTask = sut.update(task);
		
		assertThat(optionalTask).isEmpty();
		verifyNoInteractions(mapper);
		verify(validator).isValid("test");
	}
	
	@Test
	void test_updateTask_success() {
		TaskJPA taskJpa = new TaskJPA(null, "task name", "description", false, project, 3);
		entityManager.persist(taskJpa);
		project.getTasks().add(taskJpa);
		entityManager.flush();
		entityManager.detach(taskJpa);
		Long taskId = taskJpa.getId();
		Task emptyTask = new Task();
		when(mapper.map(isA(TaskJPA.class))).thenReturn(emptyTask);
		when(validator.isValid(anyString())).thenReturn(Optional.of(taskId));
		
		Task task = new Task(taskId.toString(), "another name", "test", true, 5);
		
		Optional<Task> updatedTask = sut.update(task);

		TaskJPA updatedTaskJpa = entityManager.find(TaskJPA.class, taskId);
		assertThat(updatedTaskJpa.getDescription()).matches("test");
		assertThat(updatedTaskJpa.getName()).matches("another name");
		assertThat(updatedTaskJpa.isCompleted()).isTrue();
		assertThat(updatedTaskJpa.getOrderInProject()).isEqualTo(5);
		
		assertThat(taskJpa).usingRecursiveComparison().isNotEqualTo(updatedTaskJpa);
		ArgumentCaptor<TaskJPA> taskJpaCaptor = ArgumentCaptor.forClass(TaskJPA.class);
		verify(mapper).map(taskJpaCaptor.capture());
		assertThat(taskJpaCaptor.getValue()).usingRecursiveComparison().isEqualTo(updatedTaskJpa);
		assertThat(updatedTask).containsSame(emptyTask);
		verify(validator).isValid(taskId.toString());
	}


}
