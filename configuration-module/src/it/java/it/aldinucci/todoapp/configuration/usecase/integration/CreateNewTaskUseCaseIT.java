package it.aldinucci.todoapp.configuration.usecase.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import javax.transaction.TransactionManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ProjectJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.repository.TaskJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;
import it.aldinucci.todoapp.application.port.in.CreateTaskUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exceptions.AppProjectNotFoundException;

@SpringBootTest
class CreateNewTaskUseCaseIT {

	@Autowired
	private CreateTaskUsePort createTask;
	
	@Autowired
	private ProjectJPARepository projectRepo;
	
	@Autowired
	private UserJPARepository userRepo;
	
	@Autowired
	private TaskJPARepository taskRepo;
	
	private UserJPA userFixture;
	
	@BeforeEach
	void setUp() {
		userRepo.deleteAll();
		userFixture = userRepo.save(new UserJPA("email@email.it", "username", "password"));
	}
	
	/**
	 * There's no need to test the relational integrity of the entities because that should
	 * be responsibility of the unit tests.
	 * If we want to test it anyway we need to make a @Transactional method to avoid the
	 * lazy loading, but by doing so we're not testing if the actual service does a proper
	 * transaction.
	 */
	@Test
	void test_createNewTask() {
		ProjectJPA projectJPA = projectRepo.save(new ProjectJPA("new project", userFixture));
		userFixture.getProjects().add(projectJPA);
		userRepo.save(userFixture);
		
		Task task = createTask.create(new NewTaskDTOIn("new task", "description", projectJPA.getId()));
	
		List<TaskJPA> tasks = taskRepo.findAll();
		assertThat(tasks).hasSize(1);
		
		TaskJPA taskJPA = tasks.get(0);
		
		assertThat(task.getId()).isEqualTo(taskJPA.getId());
		assertThat(task.getName()).isEqualTo(taskJPA.getName()).isEqualTo("new task");
		assertThat(task.getDescription()).isEqualTo(taskJPA.getDescription()).isEqualTo("description");
		assertThat(taskJPA.getProject()).isEqualTo(projectJPA);
	}
	
	@Test
	void test_createNewTask_whenProjectNotPresent() {
		
		NewTaskDTOIn dtoIn = new NewTaskDTOIn("task", "descr", 2);
		assertThatThrownBy(() -> createTask.create(dtoIn))
			.isInstanceOf(AppProjectNotFoundException.class);
		
		assertThat(taskRepo.findAll()).isEmpty();
	}
}
