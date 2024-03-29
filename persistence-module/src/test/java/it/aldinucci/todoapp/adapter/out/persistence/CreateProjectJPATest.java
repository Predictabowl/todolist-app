package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.application.port.out.dto.NewProjectData;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@DataJpaTest
@Import({CreateProjectJPA.class})
//@AutoConfigureTestDatabase(replace = Replace.NONE)
class CreateProjectJPATest {

	private static final String TEST_EMAIL = "test@email.it";

	@MockBean
	private AppGenericMapper<ProjectJPA, Project> mapper;
	
	@Autowired
	private CreateProjectJPA createProject;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	void test_createNewProject_whenUserNotPresent() {
		NewProjectData newProject = new NewProjectData("test name", TEST_EMAIL);
		
		assertThatThrownBy(() -> createProject.create(newProject))
			.isInstanceOf(AppUserNotFoundException.class)
			.hasMessage("User not found with email: "+TEST_EMAIL);
		
		verifyNoInteractions(mapper);
	}
	
	@Test
	void test_createNewProject_successful(){
		UserJPA userJPA = new UserJPA(null, TEST_EMAIL, "username", "password");
		entityManager.persist(userJPA);
		entityManager.flush();
		NewProjectData newProject = new NewProjectData("test name", TEST_EMAIL);
		Project project = new Project();
		when(mapper.map(isA(ProjectJPA.class))).thenReturn(project);
		
		Project returnedProject = createProject.create(newProject);
		
		ProjectJPA createdProject = entityManager.getEntityManager()
				.createQuery("from ProjectJPA",ProjectJPA.class).getSingleResult();
		
		verify(mapper).map(createdProject);
		assertThat(returnedProject).isSameAs(project);
		assertThat(createdProject.getUser()).usingRecursiveComparison().isEqualTo(userJPA);
		assertThat(createdProject.getTasks()).isEmpty();
		assertThat(userJPA.getProjects()).containsExactly(createdProject);
	}

}
