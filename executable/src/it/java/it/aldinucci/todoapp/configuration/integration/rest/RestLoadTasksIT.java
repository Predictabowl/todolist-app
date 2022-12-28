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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.response.ResponseBodyExtractionOptions;
import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ProjectJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.repository.TaskJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;
import it.aldinucci.todoapp.domain.Task;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RestLoadTasksIT {

	private static final String FIXTURE_EMAIL = "user@email.com";
	private static final String FIXTURE_PASSWORD = "somePassword";
	
	@Autowired
	private ProjectJPARepository projectRepo;

	@Autowired
	private UserJPARepository userRepo;
	
	@Autowired
	private TaskJPARepository taskRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private ObjectMapper mapper;
	
	@LocalServerPort
	private int port;
	
	private ProjectJPA project;
	

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
		userRepo.deleteAll();
		setSessionData();
	}

	@Test
	void test_loadTasks_success() throws JsonMappingException, JsonProcessingException {
		TaskJPA task1 = taskRepo.save(new TaskJPA("task 1", "descr 1", false, project));
		TaskJPA task2 = taskRepo.save(new TaskJPA("task 2", "descr 2", true, project));
		project.getTasks().add(task1);
		project.getTasks().add(task2);
		projectRepo.save(project);
		taskRepo.flush();
		projectRepo.flush();

		ResponseBodyExtractionOptions body = given()
			.auth()	.basic(FIXTURE_EMAIL, FIXTURE_PASSWORD)
			.accept(MediaType.APPLICATION_JSON_VALUE)
		.when()
			.get("/api/project/"+project.getId()+"/tasks")
		.then()
			.statusCode(200)
			.extract().body();
		
		List<Task> tasks = mapper.readValue(body.asString(), new TypeReference<List<Task>>() {});
		assertThat(tasks).containsExactly(
				new Task(task1.getId().toString(), "task 1", "descr 1", false),
				new Task(task2.getId().toString(), "task 2", "descr 2", true));
	}
	
	private void setSessionData() {
		UserJPA user = new UserJPA(FIXTURE_EMAIL, "utente", encoder.encode(FIXTURE_PASSWORD));
		user.setEnabled(true);
		userRepo.save(user);
		project = projectRepo.save(new ProjectJPA("test project", user));
		user.getProjects().add(project);
		userRepo.save(user);
		userRepo.flush();
		projectRepo.flush();
	}

}
