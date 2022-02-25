package it.aldinucci.todoapp.configuration.usecase.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static io.restassured.RestAssured.*;
import static it.aldinucci.todoapp.webcommons.config.AppBaseUrls.BASE_REST_URL;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ProjectJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;
import it.aldinucci.todoapp.application.port.in.CreateProjectUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewProjectDTOIn;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exceptions.AppUserNotFoundException;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class CreateNewProjectUSeCaseIT {

	@Autowired
	private CreateProjectUsePort createProject;
	
	@Autowired
	private UserJPARepository userRepo;
	
	@Autowired
	private ProjectJPARepository projectRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@LocalServerPort
	private int port;
	
	private static final String FIXTURE_EMAIL = "user@email.com";
	
	@BeforeEach
	void setUp() {
		RestAssured.port = port;
		userRepo.deleteAll();
		userRepo.flush();
	}
	
	@Test
	void test_createNewProject_success_old() {
		userRepo.save(new UserJPA(FIXTURE_EMAIL, "utente", "something"));
		Project project = createProject.create(new NewProjectDTOIn("new project", FIXTURE_EMAIL));
		
		List<ProjectJPA> projects = projectRepo.findAll();
		assertThat(projects).hasSize(1);
		
		ProjectJPA projectJpa = projects.get(0);
		assertThat(project.getId()).isEqualTo(projectJpa.getId());
		assertThat(project.getName()).isEqualTo(projectJpa.getName()).isEqualTo("new project");
	}
	
	@Test
	void test_createNewProject_success() {
		userRepo.save(new UserJPA(FIXTURE_EMAIL, "utente", encoder.encode("something")));
		
		Response response = given()
			.auth().basic(FIXTURE_EMAIL, "something")
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new NewProjectDTOIn("new project", FIXTURE_EMAIL))
		.when()
			.post(BASE_REST_URL+"/project/create");
		
		System.out.println("MARIO "+response.getBody().asPrettyString());
		Project project = response.getBody().as(Project.class);
		
//		Project project = createProject.create(new NewProjectDTOIn("new project", FIXTURE_EMAIL));
		
		List<ProjectJPA> projects = projectRepo.findAll();
		assertThat(projects).hasSize(1);
		
		ProjectJPA projectJpa = projects.get(0);
		assertThat(project.getId()).isEqualTo(projectJpa.getId());
		assertThat(project.getName()).isEqualTo(projectJpa.getName()).isEqualTo("new project");
	}
	
	@Test
	void test_createNewPorject_whenUserNotExists() {
		NewProjectDTOIn projectDto = new NewProjectDTOIn("new project", FIXTURE_EMAIL);
		assertThatThrownBy(() -> createProject.create(projectDto))
			.isInstanceOf(AppUserNotFoundException.class);
		
		List<ProjectJPA> projects = projectRepo.findAll();
		
		assertThat(projects).isEmpty();
	}
}
