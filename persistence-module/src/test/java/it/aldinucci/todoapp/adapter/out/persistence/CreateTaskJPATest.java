package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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
import it.aldinucci.todoapp.application.port.out.dto.NewTaskData;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exceptions.AppProjectNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@DataJpaTest
@Import({CreateTaskJPA.class})
@ExtendWith(SpringExtension.class)
class CreateTaskJPATest {

	private static final String TEST_EMAIL = "email@mail.it";
	
	@MockBean
	private AppGenericMapper<TaskJPA, Task> mapper;
	
	@Autowired
	private CreateTaskJPA createTask;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	void test_createNewTask_successful() throws AppProjectNotFoundException {
		UserJPA userJPA = new UserJPA(null, TEST_EMAIL, "username", "password");
		entityManager.persist(userJPA);
		ProjectJPA projectJPA = new ProjectJPA("project name", userJPA);
		userJPA.getProjects().add(projectJPA);
		entityManager.persist(projectJPA);
		NewTaskData newTask = new NewTaskData("task name", "task description", projectJPA.getId());
		Task task = new Task();
		when(mapper.map(isA(TaskJPA.class))).thenReturn(task);
		
		Task returnedTask = createTask.create(newTask);
		
		TaskJPA createdTask = entityManager.getEntityManager()
				.createQuery("from TaskJPA",TaskJPA.class).getSingleResult();

		verify(mapper).map(createdTask);
		assertThat(returnedTask).isSameAs(task);
		assertThat(createdTask.getProject()).usingRecursiveComparison().isEqualTo(projectJPA);
		assertThat(projectJPA.getTasks()).containsExactly(createdTask);
	}
	
	@Test
	void test_createNewTask_whenProjectNotPresent() {
		NewTaskData newTask = new NewTaskData("task name", "task description", 1L);
		
		assertThatThrownBy(() -> createTask.create(newTask))
			.isInstanceOf(AppProjectNotFoundException.class)
			.hasMessage("Project not found with id: 1");
		
		verifyNoInteractions(mapper);
	}

}
