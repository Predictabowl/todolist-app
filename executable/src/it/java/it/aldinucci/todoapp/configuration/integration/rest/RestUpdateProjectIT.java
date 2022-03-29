package it.aldinucci.todoapp.configuration.integration.rest;

import static io.restassured.RestAssured.given;
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
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ProjectJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.webcommons.dto.ProjectDataWebDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class RestUpdateProjectIT {

	@Autowired
	private UserJPARepository userRepo;

	@Autowired
	private ProjectJPARepository projectRepo;

	@Autowired
	private PasswordEncoder encoder;

	@LocalServerPort
	private int port;

	private static final String FIXTURE_EMAIL = "user@email.com";
	private static final String FIXTURE_PASSWORD = "somePassword";
	private static final String FIXTURE_URI = "/api/project/";

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
	
	@Test
	void test_updateProject_success() {
		ProjectJPA projectJpa = projectRepo.saveAndFlush(new ProjectJPA("test project", userJPA));
		userJPA.getProjects().add(projectJpa);
		userRepo.saveAndFlush(userJPA);
		
		Response response = given()
			.auth().basic(FIXTURE_EMAIL, FIXTURE_PASSWORD)
			.header("X-XSRF-TOKEN", csrfToken)
			.cookie("XSRF-TOKEN", csrfToken)
			.sessionId(sessionId)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.body(new ProjectDataWebDto("new name"))
		.when()
			.put(FIXTURE_URI+projectJpa.getId())
		.then()
			.statusCode(200)
			.extract().response();
		
		Project project = response.getBody().as(Project.class);
		assertThat(project.getId()).matches(projectJpa.getId().toString());
		assertThat(project.getName()).isEqualTo("new name");
		
		ProjectJPA updatedProject = projectRepo.findById(projectJpa.getId()).get();
		assertThat(updatedProject.getName()).matches("new name");
	}
	
	@Test
	void test_updateProject_whenProjectDontExists() {

		given()
			.auth().basic(FIXTURE_EMAIL, FIXTURE_PASSWORD)
			.header("X-XSRF-TOKEN", csrfToken)
			.cookie("XSRF-TOKEN", csrfToken)
			.sessionId(sessionId)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.body(new ProjectDataWebDto("new name"))
		.when()
			.put(FIXTURE_URI+"2")
		.then()
			.statusCode(404);
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
				.get(FIXTURE_URI);

		sessionId = response.getSessionId();
		csrfToken = response.cookie("XSRF-TOKEN");
	}
}
