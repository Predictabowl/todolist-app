package it.aldinucci.todoapp.configuration.usecase.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ProjectJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.repository.TaskJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;
import it.aldinucci.todoapp.application.port.in.DeleteTaskByIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.exceptions.AppTaskNotFoundException;

@SpringBootTest
class DeleteTaskUseCaseIT {

	@Autowired
	private DeleteTaskByIdUsePort deleteTask;

	@Autowired
	private TaskJPARepository taskRepo;

	@Autowired
	private ProjectJPARepository projectRepo;

	@Autowired
	private UserJPARepository userRepo;

	@BeforeEach
	void setUp() {
		userRepo.deleteAll();
	}

	@Test
	void test_deleteTask_success() {
		UserJPA user = userRepo.save(new UserJPA("email@email.it", "username", "password"));
		ProjectJPA project = projectRepo.save(new ProjectJPA("project name", user));
		user.getProjects().add(project);
		userRepo.save(user);
		TaskJPA task = taskRepo.save(new TaskJPA("task name", "task descr", false, project));
		project.getTasks().add(task);
		projectRepo.save(project);

		deleteTask.delete(new TaskIdDTO(task.getId()));

		assertThat(taskRepo.findAll()).isEmpty();
	}

	@Test
	void test_deleteTask_whenTaskNotPresent_shouldThrow() {
		TaskIdDTO idDTO = new TaskIdDTO(1);

		assertThatThrownBy(() -> deleteTask.delete(idDTO)).isInstanceOf(AppTaskNotFoundException.class);
	}

}
