package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.application.port.out.dto.NewTaskDTOOut;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exceptions.ProjectNotFoundException;

@DataJpaTest
@Import({CreateTaskJPA.class, ModelMapper.class})
@ExtendWith(SpringExtension.class)
class CreateTaskJPATest {

	private static final String TEST_EMAIL = "email@mail.it";
	
	@Autowired
	private CreateTaskJPA createTask;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	void test_createNewTask_successful() {
		UserJPA userJPA = new UserJPA(null, TEST_EMAIL, "username", "password");
		entityManager.persist(userJPA);
		ProjectJPA projectJPA = new ProjectJPA("project name", userJPA);
		userJPA.getProjects().add(projectJPA);
		entityManager.persist(projectJPA);
		NewTaskDTOOut newTask = new NewTaskDTOOut("task name", "task description", projectJPA.getId());
		
		Task returnedTask = createTask.create(newTask);
		
		TaskJPA createdTask = entityManager.getEntityManager()
				.createQuery("from TaskJPA",TaskJPA.class).getSingleResult();

		assertThat(returnedTask.getId()).isEqualTo(createdTask.getId());
		assertThat(returnedTask.getDescription()).isEqualTo(createdTask.getDescription());
		assertThat(returnedTask.getName()).isEqualTo(createdTask.getName());
		assertThat(createdTask.getProject()).usingRecursiveComparison().isEqualTo(projectJPA);
		assertThat(projectJPA.getTasks()).containsExactly(createdTask);
	}
	
	@Test
	void test_createNewTask_whenProjectNotPresent() {
		NewTaskDTOOut newTask = new NewTaskDTOOut("task name", "task description", 1L);
		
		assertThatThrownBy(() -> createTask.create(newTask))
			.isInstanceOf(ProjectNotFoundException.class)
			.hasMessage("Project not found with id: 1");
	}

}
