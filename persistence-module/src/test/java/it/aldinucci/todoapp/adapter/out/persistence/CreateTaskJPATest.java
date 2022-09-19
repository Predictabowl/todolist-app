package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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
import it.aldinucci.todoapp.application.port.out.dto.NewTaskData;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@DataJpaTest
@Import({CreateTaskJPA.class})
@ExtendWith(SpringExtension.class)
class CreateTaskJPATest {

	private static final String TEST_EMAIL = "email@mail.it";
	
	@MockBean
	private AppGenericMapper<TaskJPA, Task> mapper;
	
	@MockBean
	private ValidateId<Long> validator;
	
	@Autowired
	private CreateTaskJPA createTask;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	void test_createNewTask_successful() throws AppProjectNotFoundException {
		ProjectJPA projectJPA = getFixtureProject();
		entityManager.flush();
		when(validator.getValidId(anyString())).thenReturn(Optional.of(projectJPA.getId()));
		
		NewTaskData newTask = new NewTaskData("task name", "task description", false, projectJPA.getId().toString(), 3);
		Task task = new Task();
		when(mapper.map(isA(TaskJPA.class))).thenReturn(task);
		
		Task returnedTask = createTask.create(newTask);
		
		TaskJPA createdTask = entityManager.getEntityManager()
				.createQuery("from TaskJPA",TaskJPA.class).getSingleResult();

		verify(mapper).map(createdTask);
		verify(validator).getValidId(projectJPA.getId().toString());
		assertThat(returnedTask).isSameAs(task);
		assertThat(createdTask.getProject()).usingRecursiveComparison().isEqualTo(projectJPA);
		assertThat(createdTask.getOrderInProject()).isEqualTo(3);
		assertThat(createdTask.getDescription()).matches("task description");
		assertThat(createdTask.getName()).matches("task name");
		assertThat(projectJPA.getTasks()).containsExactly(createdTask);
	}

	private ProjectJPA getFixtureProject() {
		UserJPA userJPA = new UserJPA(null, TEST_EMAIL, "username", "password");
		entityManager.persist(userJPA);
		ProjectJPA projectJPA = new ProjectJPA("project name", userJPA);
		userJPA.getProjects().add(projectJPA);
		entityManager.persist(projectJPA);
		return projectJPA;
	}
	
	@Test
	void test_createNewTask_whenProjectNotPresent() {
		when(validator.getValidId(anyString())).thenReturn(Optional.of(1L));
		NewTaskData newTask = new NewTaskData("task name", "task description", false, "1", 5);
		
		assertThatThrownBy(() -> createTask.create(newTask))
			.isInstanceOf(AppProjectNotFoundException.class)
			.hasMessage("Project not found with id: 1");
		
		verifyNoInteractions(mapper);
		verify(validator).getValidId("1");
	}
	
	@Test
	void test_createNewTask_whenProjectIdIsInvalid() {
		when(validator.getValidId(anyString())).thenReturn(Optional.empty());
		NewTaskData newTask = new NewTaskData("task name", "task description", false, "invalid", 5);
		
		assertThatThrownBy(() -> createTask.create(newTask))
			.isInstanceOf(AppProjectNotFoundException.class)
			.hasMessage("Project not found with id: invalid");
		
		verifyNoInteractions(mapper);
		verify(validator).getValidId("invalid");
	}

}
