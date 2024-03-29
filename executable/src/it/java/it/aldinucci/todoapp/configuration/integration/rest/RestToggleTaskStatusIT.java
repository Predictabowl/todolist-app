package it.aldinucci.todoapp.configuration.integration.rest;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

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

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RestToggleTaskStatusIT {

	@Autowired
	private UserJPARepository userRepo;

	@Autowired
	private ProjectJPARepository projectRepo;
	
	@Autowired
	private TaskJPARepository taskRepo;

	@Autowired
	private PasswordEncoder encoder;

	@LocalServerPort
	private int port;

	private static final String FIXTURE_EMAIL = "user@email.com";
	private static final String FIXTURE_PASSWORD = "somePassword";
	private static final String FIXTURE_URI = "/api/task/";

	private String sessionId;
	private String csrfToken;

	private ProjectJPA projectJPA;
	
	@BeforeEach
	void setUp() {
		RestAssured.port = port;
		userRepo.deleteAll();
		userRepo.flush();
		setSessionData();
	}
	
	@Test
	void test_toggleTask_success() {
		TaskJPA taskJPA = taskRepo.saveAndFlush(new TaskJPA(null, "task name", "descr", false, projectJPA, 0));
		projectJPA.getTasks().add(taskJPA);
		projectRepo.saveAndFlush(projectJPA);
		
		given()
			.auth().basic(FIXTURE_EMAIL, FIXTURE_PASSWORD)
			.header("X-XSRF-TOKEN", csrfToken)
			.cookie("XSRF-TOKEN", csrfToken)
			.sessionId(sessionId)
			.accept(MediaType.APPLICATION_JSON_VALUE)
		.when()
			.put(FIXTURE_URI+taskJPA.getId()+"/completed/toggle")
		.then()
			.statusCode(200);
		
		Optional<TaskJPA> changedTask = taskRepo.findById(taskJPA.getId());
		assertThat(changedTask).isPresent();
		assertThat(changedTask.get().isCompleted()).isTrue();
	}
	
	
	@Test
	void test_toggleTask_whenTaskDontExists() {

		given()
			.auth().basic(FIXTURE_EMAIL, FIXTURE_PASSWORD)
			.header("X-XSRF-TOKEN", csrfToken)
			.cookie("XSRF-TOKEN", csrfToken)
			.sessionId(sessionId)
			.accept(MediaType.APPLICATION_JSON_VALUE)
		.when()
			.put(FIXTURE_URI+"2/completed/toggle")
		.then()
			.statusCode(404);
	}
	
	private void setSessionData() {
		UserJPA userJPA = new UserJPA(FIXTURE_EMAIL, "utente", encoder.encode(FIXTURE_PASSWORD));
		userJPA.setEnabled(true);
		userRepo.save(userJPA);
		projectJPA = projectRepo.saveAndFlush(new ProjectJPA(null, "test project", userJPA));
		userJPA.getProjects().add(projectJPA);
		userRepo.saveAndFlush(userJPA);
		
		Response response = given()
				.auth().preemptive().basic(FIXTURE_EMAIL, FIXTURE_PASSWORD)
				.accept(MediaType.APPLICATION_JSON_VALUE)
			.when()
				.get("/api/tasks");

		sessionId = response.getSessionId();
		csrfToken = response.cookie("XSRF-TOKEN");
	}
}
