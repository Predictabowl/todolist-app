package it.aldinucci.todoapp.configuration.integration.rest;

import static io.restassured.RestAssured.given;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.response.ResponseBodyExtractionOptions;
import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ProjectJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;
import it.aldinucci.todoapp.domain.Project;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class RestLoadProjectsIT {

	private static final String FIXTURE_EMAIL = "user@email.com";
	private static final String FIXTURE_PASSWORD = "somePassword";
	private static final String FIXTURE_URI = "/api/projects";
	
	@Autowired
	private ProjectJPARepository projectRepo;

	@Autowired
	private UserJPARepository userRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private ObjectMapper mapper;
	
	@LocalServerPort
	private int port;
	
	private UserJPA user;
	

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
		userRepo.deleteAll();
		setSessionData();
	}

	@Test
	void test_loadProjects_success() throws JsonMappingException, JsonProcessingException {
		ProjectJPA project1 = projectRepo.save(new ProjectJPA("test project", user));
		ProjectJPA project2 = projectRepo.save(new ProjectJPA("test 2", user));
		user.getProjects().add(project1);
		user.getProjects().add(project2);
		userRepo.save(user);

		ResponseBodyExtractionOptions body = given()
			.auth()	.basic(FIXTURE_EMAIL, FIXTURE_PASSWORD)
			.accept(MediaType.APPLICATION_JSON_VALUE)
		.when()
			.get(FIXTURE_URI)
		.then()
			.statusCode(200)
			.extract().body();
		
		List<Project> projects = mapper.readValue(body.asString(), new TypeReference<List<Project>>() {});
		assertThat(projects).containsExactly(
				new Project(project1.getId(), "test project"),
				new Project(project2.getId(), "test 2"));
	}

	private void setSessionData() {
		user = new UserJPA(FIXTURE_EMAIL, "utente", encoder.encode(FIXTURE_PASSWORD));
		user.setEnabled(true);
		userRepo.saveAndFlush(user);
	}

}
