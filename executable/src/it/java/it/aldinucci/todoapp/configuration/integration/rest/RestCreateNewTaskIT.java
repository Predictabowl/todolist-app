package it.aldinucci.todoapp.configuration.integration.rest;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ProjectJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.repository.TaskJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.webcommons.dto.TaskDataWebDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RestCreateNewTaskIT {

	private static final String FIXTURE_EMAIL = "user@email.com";
	private static final String FIXTURE_PASSWORD = "somePassword";
	private static final String BASE_URI = "/api/project/";
	
	@Autowired
	private ProjectJPARepository projectRepo;
	
	@Autowired
	private UserJPARepository userRepo;
	
	@Autowired
	private TaskJPARepository taskRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@LocalServerPort
	private int port;
	
	private String sessionId;
	private String csrfToken;
	private UserJPA userJPA;
	
	@BeforeEach
	void setUp() {
		RestAssured.port = port;
		userRepo.deleteAll();
		userRepo.flush();
		setSessionData();
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
		ProjectJPA projectJPA = projectRepo.save(new ProjectJPA("new project", userJPA));
		userJPA.getProjects().add(projectJPA);
		userRepo.save(userJPA);
		
		Response response = given()
				.auth()	.basic(FIXTURE_EMAIL, FIXTURE_PASSWORD)
				.header("X-XSRF-TOKEN", csrfToken)
				.cookie("XSRF-TOKEN", csrfToken)
				.sessionId(sessionId)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.body(new TaskDataWebDto("task name", "description"))
			.when()
				.post(BASE_URI+projectJPA.getId()+"/task")
			.then()
				.statusCode(200)
				.extract().response();
		
		Task task = response.getBody().as(Task.class);
		
		
		List<TaskJPA> tasks = taskRepo.findAll();
		assertThat(tasks).hasSize(1);
		
		TaskJPA taskJPA = tasks.get(0);
		
		assertThat(task.getId()).matches(taskJPA.getId().toString());
		assertThat(task.getName()).isEqualTo(taskJPA.getName()).isEqualTo("task name");
		assertThat(task.getDescription()).isEqualTo(taskJPA.getDescription()).isEqualTo("description");
		assertThat(taskJPA.getProject()).isEqualTo(projectJPA);
	}
	
	@Test
	void test_createNewTask_whenProjectNotPresent_shouldReturnNotFound() {
		
		given()
			.auth().basic(FIXTURE_EMAIL, FIXTURE_PASSWORD)
			.header("X-XSRF-TOKEN", csrfToken)
			.cookie("XSRF-TOKEN", csrfToken)
			.sessionId(sessionId)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.body(new TaskDataWebDto("task name", "description"))
		.when()
			.post(BASE_URI+"2/task")
		.then()
			.statusCode(404);
		
		assertThat(taskRepo.findAll()).isEmpty();
	}
	
	private void setSessionData() {
		userJPA = new UserJPA(FIXTURE_EMAIL, "utente", encoder.encode(FIXTURE_PASSWORD));
		userJPA.setEnabled(true);
		userRepo.save(userJPA);
		userRepo.flush();
		Response response = given()
				.auth().preemptive().basic(FIXTURE_EMAIL, FIXTURE_PASSWORD)
				.accept(MediaType.APPLICATION_JSON_VALUE)
			.when()
				.get("/api/project");

		sessionId = response.getSessionId();
		csrfToken = response.cookie("XSRF-TOKEN");
	}
}
