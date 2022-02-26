package it.aldinucci.todoapp.configuration.integration.rest;

import static io.restassured.RestAssured.given;
import static it.aldinucci.todoapp.webcommons.config.AppBaseURIs.BASE_REST_URI;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ProjectJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.repository.TaskJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class RestDeleteTaskIT {

	private static final String FIXTURE_EMAIL = "user@email.com";
	private static final String FIXTURE_PASSWORD = "somePassword";
	private static final String FIXTURE_URI = BASE_REST_URI + "/task/1";
	
	@Autowired
	private TaskJPARepository taskRepo;

	@Autowired
	private ProjectJPARepository projectRepo;

	@Autowired
	private UserJPARepository userRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@LocalServerPort
	private int port;
	
	private String sessionId;
	private String csrfToken;
	private UserJPA user;
	private ProjectJPA project;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
		userRepo.deleteAll();
		userRepo.flush();
		setSessionData();
	}

	@Test
	void test_deleteTask_success() {
		TaskJPA task = taskRepo.save(new TaskJPA("task name", "task descr", false, project));
		project.getTasks().add(task);
		projectRepo.save(project);

		given()
			.auth()	.basic(FIXTURE_EMAIL, FIXTURE_PASSWORD)
			.header("X-XSRF-TOKEN", csrfToken)
			.cookie("XSRF-TOKEN", csrfToken)
			.sessionId(sessionId)
		.when()
			.delete(BASE_REST_URI+"/task/"+task.getId())
		.then()
			.statusCode(200);
		
		assertThat(taskRepo.findAll()).isEmpty();
	}

	@Test
	void test_deleteTask_whenTaskNotPresent_shouldReturnNotFound() {
		given()
			.auth()	.basic(FIXTURE_EMAIL, FIXTURE_PASSWORD)
			.header("X-XSRF-TOKEN", csrfToken)
			.cookie("XSRF-TOKEN", csrfToken)
			.sessionId(sessionId)
		.when()
			.delete(FIXTURE_URI)
		.then()
			.statusCode(404);
	}
	
	private void setSessionData() {
		user = userRepo.save(new UserJPA(FIXTURE_EMAIL, "utente", encoder.encode(FIXTURE_PASSWORD)));
		project = projectRepo.save(new ProjectJPA("project name", user));
		user.getProjects().add(project);
		userRepo.save(user);
		userRepo.flush();
		projectRepo.flush();
		
		Response response = given()
				.auth().preemptive().basic(FIXTURE_EMAIL, FIXTURE_PASSWORD)
				.accept(MediaType.APPLICATION_JSON_VALUE)
			.when()
				.get(FIXTURE_URI);

		sessionId = response.getSessionId();
		csrfToken = response.cookie("XSRF-TOKEN");
	}

}
