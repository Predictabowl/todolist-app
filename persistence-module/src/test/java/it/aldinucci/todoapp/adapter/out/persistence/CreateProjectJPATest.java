package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.application.port.out.dto.NewProjectDTOOut;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.exceptions.UserPersistenceException;

@DataJpaTest
@Import({CreateProjectJPA.class, ModelMapper.class})
@ExtendWith(SpringExtension.class)
class CreateProjectJPATest {

	private static final String TEST_EMAIL = "test@email.it";

	@Autowired
	private CreateProjectJPA createProject;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	void test_createNewProject_successful() {
		UserJPA userJPA = new UserJPA(null, TEST_EMAIL, "username", "password");
		entityManager.persist(userJPA);
		NewProjectDTOOut newProject = new NewProjectDTOOut("test name", TEST_EMAIL, "no description");
		
		Project returnedProject = createProject.create(newProject);
		
		ProjectJPA createdProject = entityManager.getEntityManager()
				.createQuery("from ProjectJPA",ProjectJPA.class).getSingleResult();
		
		assertThat(returnedProject.getId()).isEqualTo(createdProject.getId());
		assertThat(returnedProject.getName()).isEqualTo(createdProject.getName());
		assertThat(createdProject.getUser()).usingRecursiveComparison().isEqualTo(userJPA);
		assertThat(createdProject.getTasks()).isEmpty();
	}
	
	@Test
	void test_createNewProject_whenUserNotPresent() {
		NewProjectDTOOut newProject = new NewProjectDTOOut("test name", TEST_EMAIL, "no description");
		
		assertThatThrownBy(() -> createProject.create(newProject))
			.isInstanceOf(UserPersistenceException.class)
			.hasMessage("User not found with email: "+TEST_EMAIL);
	}

}
