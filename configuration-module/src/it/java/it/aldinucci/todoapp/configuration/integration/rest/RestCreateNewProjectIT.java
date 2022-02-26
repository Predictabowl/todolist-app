package it.aldinucci.todoapp.configuration.integration.rest;

import static io.restassured.RestAssured.given;
import static it.aldinucci.todoapp.webcommons.config.AppBaseURIs.BASE_REST_URI;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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
import it.aldinucci.todoapp.application.port.in.dto.NewProjectDTOIn;
import it.aldinucci.todoapp.domain.Project;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class RestCreateNewProjectIT {

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
	private static final String FIXTURE_URI = BASE_REST_URI + "/project/create";

	private String sessionId;
	private String csrfToken;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
		userRepo.deleteAll();
		userRepo.flush();
		setSessionData();
	}

	@Test
	void test_createNewProject_success() {

		Response response = given()
				.auth()	.basic(FIXTURE_EMAIL, FIXTURE_PASSWORD) // this is superfluous after obtaining the sessionId
				.header("X-XSRF-TOKEN", csrfToken)
				.cookie("XSRF-TOKEN", csrfToken) // both the header AND the cookie are needed
				.sessionId(sessionId)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.body(new NewProjectDTOIn("new project", FIXTURE_EMAIL))
			.when()
				.post(FIXTURE_URI)
			.then()
				.statusCode(200)
				.extract().response();

		Project project = response.getBody().as(Project.class);

		List<ProjectJPA> projects = projectRepo.findAll();
		assertThat(projects).hasSize(1);

		ProjectJPA projectJpa = projects.get(0);
		assertThat(project.getId()).isEqualTo(projectJpa.getId());
		assertThat(project.getName()).isEqualTo(projectJpa.getName()).isEqualTo("new project");
	}

	private void setSessionData() {
		userRepo.save(new UserJPA(FIXTURE_EMAIL, "utente", encoder.encode(FIXTURE_PASSWORD)));
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
